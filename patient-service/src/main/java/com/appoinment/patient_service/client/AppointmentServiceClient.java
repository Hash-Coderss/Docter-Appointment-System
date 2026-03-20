package com.appoinment.patient_service.client;

import com.appoinment.patient_service.dto.external.AppointmentPayload;
import com.appoinment.patient_service.dto.external.BookAppointmentPayload;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "appointment-service", url = "${appointment-service.url}")
public interface AppointmentServiceClient {

    @PostMapping("/appointments/book")
    AppointmentPayload bookAppointment(@RequestBody BookAppointmentPayload request);

    @GetMapping("/appointments/patient")
    List<AppointmentPayload> getPatientAppointments();
}
