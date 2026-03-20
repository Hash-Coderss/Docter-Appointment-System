package com.appoinment.patient_service.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DoctorSlotResponse {

    private Long doctorId;
    private LocalDate date;
    private List<String> timeSlots;
}
