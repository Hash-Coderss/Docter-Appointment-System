package com.appoinment.patient_service.dto.external;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDoctorPayload {

    private Long doctorId;
    private Long userId;
    private String name;
    private String email;
    private String specialization;
    private Integer experience;
    private String hospitalName;
}
