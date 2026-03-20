package com.appoinment.patient_service.service.impl;

import com.appoinment.patient_service.client.AppointmentServiceClient;
import com.appoinment.patient_service.client.DoctorServiceClient;
import com.appoinment.patient_service.client.UserServiceClient;
import com.appoinment.patient_service.dto.external.AppointmentPayload;
import com.appoinment.patient_service.dto.external.BookAppointmentPayload;
import com.appoinment.patient_service.dto.external.CurrentUserPayload;
import com.appoinment.patient_service.dto.external.DoctorAvailabilityPayload;
import com.appoinment.patient_service.dto.external.UserDoctorPayload;
import com.appoinment.patient_service.dto.request.BookFromPatientRequest;
import com.appoinment.patient_service.dto.response.AppointmentResponse;
import com.appoinment.patient_service.dto.response.DoctorSlotResponse;
import com.appoinment.patient_service.dto.response.DoctorSummaryResponse;
import com.appoinment.patient_service.entity.PatientAppointment;
import com.appoinment.patient_service.exception.ForbiddenOperationException;
import com.appoinment.patient_service.exception.ResourceNotFoundException;
import com.appoinment.patient_service.repository.PatientAppointmentRepository;
import com.appoinment.patient_service.service.PatientService;
import feign.FeignException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final UserServiceClient userServiceClient;
    private final DoctorServiceClient doctorServiceClient;
    private final AppointmentServiceClient appointmentServiceClient;
    private final PatientAppointmentRepository patientAppointmentRepository;

    @Override
    public List<DoctorSummaryResponse> getDoctorsBySpecialization(String specialization) {
        List<UserDoctorPayload> doctors = userServiceClient.getDoctorsBySpecialization(specialization);
        return doctors.stream()
                .map(this::toDoctorSummary)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorSlotResponse> getDoctorSlots(Long doctorId) {
        List<DoctorAvailabilityPayload> availabilities = doctorServiceClient.getDoctorAvailability(doctorId);
        if (availabilities == null || availabilities.isEmpty()) {
            throw new ResourceNotFoundException("No availability found for doctorId: " + doctorId);
        }
        return availabilities.stream()
                .map(this::toDoctorSlot)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse bookAppointment(BookFromPatientRequest request, Authentication authentication) {
        String email = getEmail(authentication);
        assertPatientRole(authentication);

        CurrentUserPayload me = userServiceClient.getCurrentUser();
        if (me == null || me.getId() == null) {
            throw new ResourceNotFoundException("Authenticated patient profile not found");
        }
        if (me.getEmail() != null && !me.getEmail().equalsIgnoreCase(email)) {
            throw new ForbiddenOperationException("Authenticated email does not match patient profile");
        }

        BookAppointmentPayload payload = BookAppointmentPayload.builder()
                .doctorId(request.getDoctorId())
                .date(request.getDate())
                .timeSlot(request.getTimeSlot())
                .build();

        try {
            AppointmentPayload booked = appointmentServiceClient.bookAppointment(payload);
            syncLocalAppointment(booked, email);
            return toAppointmentResponse(booked);
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("Doctor or slot not found for booking request");
        }
    }

    @Override
    public List<AppointmentResponse> getMyAppointments(Authentication authentication) {
        String email = getEmail(authentication);
        assertPatientRole(authentication);

        List<AppointmentPayload> appointments = appointmentServiceClient.getPatientAppointments();
        if (appointments == null || appointments.isEmpty()) {
            return patientAppointmentRepository.findByPatientEmailOrderByDateDesc(email)
                    .stream()
                    .map(this::toAppointmentResponse)
                    .collect(Collectors.toList());
        }

        appointments.forEach(appointment -> syncLocalAppointment(appointment, email));
        return appointments.stream()
                .map(this::toAppointmentResponse)
                .collect(Collectors.toList());
    }

    private void syncLocalAppointment(AppointmentPayload payload, String email) {
        if (payload == null || payload.getId() == null) {
            return;
        }

        PatientAppointment entity = patientAppointmentRepository.findByAppointmentId(payload.getId())
                .orElseGet(PatientAppointment::new);
        entity.setAppointmentId(payload.getId());
        entity.setDoctorId(payload.getDoctorId());
        entity.setPatientId(payload.getPatientId() != null ? payload.getPatientId() : 0L);
        entity.setPatientEmail(email);
        entity.setDate(payload.getDate());
        entity.setTimeSlot(payload.getTimeSlot());
        entity.setStatus(payload.getStatus());
        patientAppointmentRepository.save(entity);
    }

    private DoctorSummaryResponse toDoctorSummary(UserDoctorPayload payload) {
        Long doctorId = payload.getDoctorId() != null ? payload.getDoctorId() : payload.getUserId();
        return DoctorSummaryResponse.builder()
                .doctorId(doctorId)
                .name(payload.getName())
                .email(payload.getEmail())
                .specialization(payload.getSpecialization())
                .experience(payload.getExperience())
                .hospitalName(payload.getHospitalName())
                .build();
    }

    private DoctorSlotResponse toDoctorSlot(DoctorAvailabilityPayload payload) {
        return DoctorSlotResponse.builder()
                .doctorId(payload.getDoctorId())
                .date(payload.getDate())
                .timeSlots(payload.getTimeSlots())
                .build();
    }

    private AppointmentResponse toAppointmentResponse(AppointmentPayload payload) {
        return AppointmentResponse.builder()
                .id(payload.getId())
                .doctorId(payload.getDoctorId())
                .patientId(payload.getPatientId())
                .date(payload.getDate())
                .timeSlot(payload.getTimeSlot())
                .status(payload.getStatus())
                .build();
    }

    private AppointmentResponse toAppointmentResponse(PatientAppointment payload) {
        return AppointmentResponse.builder()
                .id(payload.getAppointmentId())
                .doctorId(payload.getDoctorId())
                .patientId(payload.getPatientId())
                .date(payload.getDate())
                .timeSlot(payload.getTimeSlot())
                .status(payload.getStatus())
                .build();
    }

    private String getEmail(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new ForbiddenOperationException("Invalid authentication context");
        }
        return authentication.getName();
    }

    private void assertPatientRole(Authentication authentication) {
        Set<String> roles = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toSet());
        if (!roles.contains("ROLE_PATIENT")) {
            throw new ForbiddenOperationException("Only PATIENT role can access this resource");
        }
    }
}
