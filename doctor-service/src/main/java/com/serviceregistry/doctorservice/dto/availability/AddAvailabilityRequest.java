package com.serviceregistry.doctorservice.dto.availability;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record AddAvailabilityRequest(
        @NotNull LocalDate date,
        @NotEmpty List<@NotEmpty String> timeSlots
) {
}
