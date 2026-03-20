package com.serviceregistry.doctorservice.dto.availability;

import java.time.LocalDate;
import java.util.List;

public record DoctorAvailabilityResponse(
        Long id,
        Long doctorId,
        LocalDate date,
        List<String> timeSlots
) {
}
