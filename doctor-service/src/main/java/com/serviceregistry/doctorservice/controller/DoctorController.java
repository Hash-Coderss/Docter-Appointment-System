package com.serviceregistry.doctorservice.controller;

import com.serviceregistry.doctorservice.dto.availability.AddAvailabilityRequest;
import com.serviceregistry.doctorservice.dto.availability.DoctorAvailabilityResponse;
import com.serviceregistry.doctorservice.dto.appointment.AppointmentResponse;
import com.serviceregistry.doctorservice.exception.BadRequestException;
import com.serviceregistry.doctorservice.service.DoctorAppointmentService;
import com.serviceregistry.doctorservice.service.DoctorAvailabilityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorAvailabilityService availabilityService;
    private final DoctorAppointmentService appointmentService;

    public DoctorController(DoctorAvailabilityService availabilityService, DoctorAppointmentService appointmentService) {
        this.availabilityService = availabilityService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/availability")
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
    public DoctorAvailabilityResponse addAvailability(
            @RequestHeader(value = "X-User-Id", required = false) Long doctorId,
            @Valid @RequestBody AddAvailabilityRequest request
    ) {
        return availabilityService.addOrUpdateAvailability(requireDoctorId(doctorId), request);
    }

    @GetMapping("/availability/{doctorId}")
    public List<DoctorAvailabilityResponse> getAvailability(@PathVariable Long doctorId) {
        return availabilityService.getAvailability(doctorId);
    }

    @GetMapping("/appointments")
    public List<AppointmentResponse> getMyAppointments(
            @RequestHeader(value = "X-User-Id", required = false) Long doctorId
    ) {
        return appointmentService.getMyAppointments(requireDoctorId(doctorId));
    }

    @PutMapping("/appointment/{id}/accept")
    public AppointmentResponse accept(@PathVariable("id") Long appointmentId) {
        return appointmentService.accept(appointmentId);
    }

    @DeleteMapping("/appointment/{id}/reject")
    public AppointmentResponse reject(@PathVariable("id") Long appointmentId) {
        return appointmentService.reject(appointmentId);
    }

    private Long requireDoctorId(Long doctorId) {
        if (doctorId == null) {
            throw new BadRequestException("Missing required header: X-User-Id");
        }
        return doctorId;
    }
}
