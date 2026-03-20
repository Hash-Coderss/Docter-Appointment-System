package com.appoinment.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {

    private Long userId;
    private String name;
    private String email;
    private String role;

    private String specialization;
    private Integer experience;
    private String hospitalName;

    private Integer age;
    private String gender;
}
