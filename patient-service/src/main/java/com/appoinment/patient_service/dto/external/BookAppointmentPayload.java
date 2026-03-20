package com.appoinment.patient_service.dto.external;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookAppointmentPayload {

    private Long doctorId;
    private LocalDate date;
    private String timeSlot;
}
