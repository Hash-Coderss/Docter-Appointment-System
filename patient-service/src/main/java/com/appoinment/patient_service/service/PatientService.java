package com.appoinment.patient_service.service;

import com.appoinment.patient_service.dto.request.BookFromPatientRequest;
import com.appoinment.patient_service.dto.response.AppointmentResponse;
import com.appoinment.patient_service.dto.response.DoctorSlotResponse;
import com.appoinment.patient_service.dto.response.DoctorSummaryResponse;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface PatientService {

    List<DoctorSummaryResponse> getDoctorsBySpecialization(String specialization);

    List<DoctorSlotResponse> getDoctorSlots(Long doctorId);

    AppointmentResponse bookAppointment(BookFromPatientRequest request, Authentication authentication);

    List<AppointmentResponse> getMyAppointments(Authentication authentication);
}
