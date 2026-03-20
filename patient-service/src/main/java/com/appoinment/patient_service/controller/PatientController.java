package com.appoinment.patient_service.controller;

import com.appoinment.patient_service.dto.request.BookFromPatientRequest;
import com.appoinment.patient_service.dto.response.AppointmentResponse;
import com.appoinment.patient_service.dto.response.DoctorSlotResponse;
import com.appoinment.patient_service.dto.response.DoctorSummaryResponse;
import com.appoinment.patient_service.service.PatientService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/doctors")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<DoctorSummaryResponse>> getDoctorsBySpecialization(
            @RequestParam("specialization") String specialization) {
        return ResponseEntity.ok(patientService.getDoctorsBySpecialization(specialization));
    }

    @GetMapping("/doctor/{doctorId}/slots")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<DoctorSlotResponse>> getDoctorSlots(@PathVariable Long doctorId) {
        return ResponseEntity.ok(patientService.getDoctorSlots(doctorId));
    }

    @PostMapping("/book")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponse> bookAppointment(@Valid @RequestBody BookFromPatientRequest request,
                                                               Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patientService.bookAppointment(request, authentication));
    }

    @GetMapping("/my-appointments")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(Authentication authentication) {
        return ResponseEntity.ok(patientService.getMyAppointments(authentication));
    }
}
