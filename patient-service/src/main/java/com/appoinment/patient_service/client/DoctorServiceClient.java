package com.appoinment.patient_service.client;

import com.appoinment.patient_service.dto.external.DoctorAvailabilityPayload;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctor-service", url = "${doctor-service.url}")
public interface DoctorServiceClient {

    @GetMapping("/doctor/availability/{doctorId}")
    List<DoctorAvailabilityPayload> getDoctorAvailability(@PathVariable("doctorId") Long doctorId);
}
