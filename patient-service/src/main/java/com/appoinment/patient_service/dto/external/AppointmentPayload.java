package com.appoinment.patient_service.dto.external;

import com.appoinment.patient_service.entity.enums.AppointmentStatus;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentPayload {

    private Long id;
    private Long doctorId;
    private Long patientId;
    private LocalDate date;
    private String timeSlot;
    private AppointmentStatus status;
}
