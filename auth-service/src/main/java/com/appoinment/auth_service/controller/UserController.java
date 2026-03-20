package com.appoinment.auth_service.controller;

import com.appoinment.auth_service.dto.DoctorListResponse;
import com.appoinment.auth_service.dto.UserProfileResponse;
import com.appoinment.auth_service.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMe(Authentication auth) {
        return ResponseEntity.ok(userService.getCurrentUser(auth.getName()));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorListResponse>> getDoctors(@RequestParam String specialization) {
        return ResponseEntity.ok(userService.getDoctorsBySpecialization(specialization));
    }
}
