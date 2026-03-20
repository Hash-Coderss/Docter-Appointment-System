package com.appointment.appointment_service.controller;

import com.appointment.appointment_service.dto.AppointmentResponse;
import com.appointment.appointment_service.dto.BookAppointmentRequest;
import com.appointment.appointment_service.dto.UpdateStatusRequest;
import com.appointment.appointment_service.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * Book an appointment for the authenticated patient
     * POST /appointments/book
     * Requires: PATIENT role
     * @param request BookAppointmentRequest containing doctor ID, date, and time slot
     * @param authentication Authenticated user details from JWT token
     * @return AppointmentResponse with created appointment
     */
    @PostMapping("/book")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponse> bookAppointment(
            @Valid @RequestBody BookAppointmentRequest request,
            Authentication authentication) {
        // Extract patient ID from authentication (in a real scenario, this would come from JWT claims)
        // For now, using a placeholder - in production, extract from JWT token claims
        Long patientId = extractUserIdFromAuthentication(authentication);
        
        AppointmentResponse response = appointmentService.bookAppointment(patientId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get all appointments for the authenticated patient
     * GET /appointments/patient
     * Requires: PATIENT role
     * @param authentication Authenticated user details from JWT token
     * @return List of AppointmentResponse for the patient
     */
    @GetMapping("/patient")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<AppointmentResponse>> getPatientAppointments(
            Authentication authentication) {
        Long patientId = extractUserIdFromAuthentication(authentication);
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Get all appointments for a specific doctor
     * GET /appointments/doctor/{doctorId}
     * Requires: DOCTOR role (can only view their own appointments)
     * @param doctorId Doctor ID
     * @param authentication Authenticated user details from JWT token
     * @return List of AppointmentResponse for the doctor
     */
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentResponse>> getDoctorAppointments(
            @PathVariable Long doctorId,
            Authentication authentication) {
        // In production, validate that the authenticated doctor ID matches the path parameter
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Update appointment status (ACCEPTED or REJECTED)
     * PUT /appointments/{id}/status
     * Requires: DOCTOR role
     * @param id Appointment ID
     * @param request UpdateStatusRequest containing new status
     * @param authentication Authenticated user details from JWT token
     * @return Updated AppointmentResponse
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AppointmentResponse> updateAppointmentStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request,
            Authentication authentication) {
        AppointmentResponse response = appointmentService.updateAppointmentStatus(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get appointment details by ID
     * GET /appointments/{id}
     * @param id Appointment ID
     * @return AppointmentResponse
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        AppointmentResponse response = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Helper method to extract user ID from Authentication object
     * In production, this should extract the ID from JWT token claims
     * @param authentication Authenticated user details
     * @return User ID (placeholder implementation)
     */
    private Long extractUserIdFromAuthentication(Authentication authentication) {
        // This is a placeholder implementation
        // In production, extract from JWT token claims or user principal
        // Example: authentication.getPrincipal() should return user details with ID
        String username = authentication.getName();
        // For now, return a placeholder - this should be replaced with actual JWT claim extraction
        return 1L; // This should be replaced with actual user ID from JWT
    }
}
