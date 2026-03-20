package com.appointment.appointment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * Feign client for Doctor Service
 * Used to check doctor availability and validate appointment slots
 */
@FeignClient(name = "doctor-service", url = "${feign.client.config.doctor-service.url:http://localhost:8081}")
public interface DoctorServiceClient {

    /**
     * Get available time slots for a doctor on a specific date
     * @param doctorId Doctor ID
     * @param date Date to check availability
     * @return List of available time slots
     */
    @GetMapping("/doctor/availability/{doctorId}")
    ResponseEntity<DoctorAvailabilityDto> getAvailability(
            @PathVariable Long doctorId,
            @RequestParam LocalDate date
    );
}
