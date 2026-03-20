package com.appoinment.patient_service.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookFromPatientRequest {

    @NotNull(message = "doctorId is required")
    private Long doctorId;

    @NotNull(message = "date is required")
    @FutureOrPresent(message = "date must be today or future")
    private LocalDate date;

    @NotBlank(message = "timeSlot is required")
    private String timeSlot;
}
