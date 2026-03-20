package com.appoinment.patient_service.dto.external;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentUserPayload {

    private Long id;
    private String name;
    private String email;
    private String role;
}
