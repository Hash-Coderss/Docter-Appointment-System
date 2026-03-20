package com.serviceregistry.doctorservice.dto.appointment;

import java.time.LocalDate;

public record AppointmentResponse(
        Long id,
        Long doctorId,
        Long patientId,
        LocalDate date,
        String timeSlot,
        AppointmentStatus status
) {
}
