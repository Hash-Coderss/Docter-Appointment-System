package com.appoinment.patient_service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DoctorSummaryResponse {

    private Long doctorId;
    private String name;
    private String email;
    private String specialization;
    private Integer experience;
    private String hospitalName;
}
