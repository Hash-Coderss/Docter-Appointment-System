package com.serviceregistry.doctorservice.service;

import com.serviceregistry.doctorservice.client.AppointmentClient;
import com.serviceregistry.doctorservice.dto.appointment.AppointmentResponse;
import com.serviceregistry.doctorservice.dto.appointment.AppointmentStatus;
import com.serviceregistry.doctorservice.dto.appointment.UpdateAppointmentStatusRequest;
import com.serviceregistry.doctorservice.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorAppointmentService {

    private final AppointmentClient appointmentClient;

    public DoctorAppointmentService(AppointmentClient appointmentClient) {
        this.appointmentClient = appointmentClient;
    }

    public List<AppointmentResponse> getMyAppointments(Long doctorId) {
        if (doctorId == null) {
            throw new BadRequestException("doctorId is required");
        }
        return appointmentClient.getAppointmentsForDoctor(doctorId);
    }

    public AppointmentResponse accept(Long appointmentId) {
        return appointmentClient.updateAppointmentStatus(
                appointmentId,
                new UpdateAppointmentStatusRequest(AppointmentStatus.ACCEPTED)
        );
    }

    public AppointmentResponse reject(Long appointmentId) {
        return appointmentClient.updateAppointmentStatus(
                appointmentId,
                new UpdateAppointmentStatusRequest(AppointmentStatus.REJECTED)
        );
    }
}
