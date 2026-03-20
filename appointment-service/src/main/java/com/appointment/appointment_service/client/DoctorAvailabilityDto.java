package com.appointment.appointment_service.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for Doctor Service availability response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAvailabilityDto {
    private Long id;
    private Long doctorId;
    private LocalDate date;
    private List<String> timeSlots;
}
