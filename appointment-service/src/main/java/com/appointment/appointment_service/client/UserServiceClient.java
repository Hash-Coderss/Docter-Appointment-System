package com.appointment.appointment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for User Service
 * Used to validate if a patient or doctor exists in the system
 */
@FeignClient(name = "user-service", url = "${feign.client.config.user-service.url:http://localhost:8080}")
public interface UserServiceClient {

    /**
     * Get user details by user ID
     * Used to validate if a user exists before booking an appointment
     * @param userId User ID
     * @return User details
     */
    @GetMapping("/users/{userId}")
    ResponseEntity<UserDto> getUserById(@PathVariable Long userId);
}
