package com.appoinment.auth_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appoinment.auth_service.entity.DoctorProfile;

public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {

    List<DoctorProfile> findBySpecializationIgnoreCase(String specialization);
}
