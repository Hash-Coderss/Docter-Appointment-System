package com.appoinment.patient_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDoctorRequest {

    @NotBlank(message = "specialization is required")
    private String specialization;
}
