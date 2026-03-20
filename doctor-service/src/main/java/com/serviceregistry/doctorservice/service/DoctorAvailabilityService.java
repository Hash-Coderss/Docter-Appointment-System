package com.serviceregistry.doctorservice.service;

import com.serviceregistry.doctorservice.dto.availability.AddAvailabilityRequest;
import com.serviceregistry.doctorservice.dto.availability.DoctorAvailabilityResponse;
import com.serviceregistry.doctorservice.exception.BadRequestException;
import com.serviceregistry.doctorservice.model.DoctorAvailability;
import com.serviceregistry.doctorservice.repository.DoctorAvailabilityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepository repository;

    public DoctorAvailabilityService(DoctorAvailabilityRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public DoctorAvailabilityResponse addOrUpdateAvailability(Long doctorId, AddAvailabilityRequest request) {
        if (doctorId == null) {
            throw new BadRequestException("doctorId is required");
        }

        DoctorAvailability availability = repository
                .findByDoctorIdAndDate(doctorId, request.date())
                .orElseGet(() -> new DoctorAvailability(doctorId, request.date(), List.of()));

        availability.setTimeSlots(request.timeSlots());

        DoctorAvailability saved = repository.save(availability);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<DoctorAvailabilityResponse> getAvailability(Long doctorId) {
        if (doctorId == null) {
            throw new BadRequestException("doctorId is required");
        }
        return repository.findByDoctorIdOrderByDateAsc(doctorId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private DoctorAvailabilityResponse toResponse(DoctorAvailability availability) {
        return new DoctorAvailabilityResponse(
                availability.getId(),
                availability.getDoctorId(),
                availability.getDate(),
                List.copyOf(availability.getTimeSlots())
        );
    }
}
