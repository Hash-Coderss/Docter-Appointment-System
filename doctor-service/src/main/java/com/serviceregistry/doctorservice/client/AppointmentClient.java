package com.serviceregistry.doctorservice.client;

import com.serviceregistry.doctorservice.dto.appointment.AppointmentResponse;
import com.serviceregistry.doctorservice.dto.appointment.UpdateAppointmentStatusRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "Appointment-Service")
public interface AppointmentClient {

    @GetMapping("/appointments/doctor/{doctorId}")
    List<AppointmentResponse> getAppointmentsForDoctor(@PathVariable("doctorId") Long doctorId);

    @PutMapping("/appointments/{id}/status")
    AppointmentResponse updateAppointmentStatus(
            @PathVariable("id") Long appointmentId,
            @RequestBody UpdateAppointmentStatusRequest request
    );
}
