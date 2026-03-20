package com.serviceregistry.doctorservice.repository;

import com.serviceregistry.doctorservice.model.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {

    List<DoctorAvailability> findByDoctorIdOrderByDateAsc(Long doctorId);

    Optional<DoctorAvailability> findByDoctorIdAndDate(Long doctorId, LocalDate date);
}
