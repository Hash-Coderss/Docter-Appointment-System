package com.appointment.appointment_service.service;

import com.appointment.appointment_service.dto.AppointmentResponse;
import com.appointment.appointment_service.dto.BookAppointmentRequest;
import com.appointment.appointment_service.dto.UpdateStatusRequest;
import com.appointment.appointment_service.entity.Appointment;
import com.appointment.appointment_service.entity.AppointmentStatus;
import com.appointment.appointment_service.exception.AppointmentNotFoundException;
import com.appointment.appointment_service.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    /**
     * Book an appointment for a patient
     * @param patientId Patient ID extracted from JWT token
     * @param request BookAppointmentRequest containing doctor ID, date, and time slot
     * @return AppointmentResponse with appointment details
     */
    public AppointmentResponse bookAppointment(Long patientId, BookAppointmentRequest request) {
        // Validate that date is in the future
        if (request.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Appointment date must be in the future");
        }

        // Create new appointment with PENDING status
        Appointment appointment = new Appointment();
        appointment.setDoctorId(request.getDoctorId());
        appointment.setPatientId(patientId);
        appointment.setDate(request.getDate());
        appointment.setTimeSlot(request.getTimeSlot());
        appointment.setStatus(AppointmentStatus.PENDING);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToResponse(savedAppointment);
    }

    /**
     * Get all appointments for a patient
     * @param patientId Patient ID extracted from JWT token
     * @return List of AppointmentResponse
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all appointments for a doctor
     * @param doctorId Doctor ID
     * @return List of AppointmentResponse
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update appointment status (ACCEPTED or REJECTED)
     * @param appointmentId Appointment ID to update
     * @param request UpdateStatusRequest containing new status
     * @return AppointmentResponse with updated details
     */
    public AppointmentResponse updateAppointmentStatus(Long appointmentId, UpdateStatusRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));

        // Only allow updating to ACCEPT or REJECTED
        if (request.getStatus() == AppointmentStatus.PENDING) {
            throw new IllegalArgumentException("Cannot update appointment to PENDING status");
        }

        appointment.setStatus(request.getStatus());
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return mapToResponse(updatedAppointment);
    }

    /**
     * Get appointment by ID
     * @param appointmentId Appointment ID
     * @return AppointmentResponse
     */
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));
        return mapToResponse(appointment);
    }

    /**
     * Check available slots for a doctor on a specific date
     * @param doctorId Doctor ID
     * @param date Date to check availability
     * @return List of booked time slots
     */
    @Transactional(readOnly = true)
    public List<String> getBookedSlots(Long doctorId, LocalDate date) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDate(doctorId, date);
        return appointments.stream()
                .map(Appointment::getTimeSlot)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to convert Appointment entity to AppointmentResponse DTO
     */
    private AppointmentResponse mapToResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setDoctorId(appointment.getDoctorId());
        response.setPatientId(appointment.getPatientId());
        response.setDate(appointment.getDate());
        response.setTimeSlot(appointment.getTimeSlot());
        response.setStatus(appointment.getStatus());
        response.setCreatedAt(appointment.getCreatedAt());
        response.setUpdatedAt(appointment.getUpdatedAt());
        return response;
    }
}
