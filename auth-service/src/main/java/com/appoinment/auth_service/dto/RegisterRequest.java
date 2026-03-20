package com.appoinment.auth_service.dto;

import lombok.Data;

@Data
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private String role;

    private String specialization;
    private String experience;
    private String hospitalName;

    private String age;
    private String gender;
}
