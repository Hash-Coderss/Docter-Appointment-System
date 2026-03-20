package com.appoinment.patient_service.dto.response;

import com.appoinment.patient_service.entity.enums.AppointmentStatus;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AppointmentResponse {

    private Long id;
    private Long doctorId;
    private Long patientId;
    private LocalDate date;
    private String timeSlot;
    private AppointmentStatus status;
}
