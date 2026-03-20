package com.appoinment.patient_service.dto.external;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorAvailabilityPayload {

    private Long id;
    private Long doctorId;
    private LocalDate date;
    private List<String> timeSlots;
}
