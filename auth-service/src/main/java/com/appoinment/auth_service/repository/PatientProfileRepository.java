package com.appoinment.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appoinment.auth_service.entity.PatientProfile;

public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {
}
