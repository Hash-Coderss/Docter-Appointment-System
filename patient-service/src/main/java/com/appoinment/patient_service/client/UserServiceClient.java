package com.appoinment.patient_service.client;

import com.appoinment.patient_service.dto.external.CurrentUserPayload;
import com.appoinment.patient_service.dto.external.UserDoctorPayload;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-user-service", url = "${auth-user-service.url}")
public interface UserServiceClient {

    @GetMapping("/users/doctors")
    List<UserDoctorPayload> getDoctorsBySpecialization(@RequestParam("specialization") String specialization);

    @GetMapping("/users/me")
    CurrentUserPayload getCurrentUser();
}
