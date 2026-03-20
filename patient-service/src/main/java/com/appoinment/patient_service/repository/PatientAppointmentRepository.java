package com.appoinment.patient_service.repository;

import com.appoinment.patient_service.entity.PatientAppointment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientAppointmentRepository extends JpaRepository<PatientAppointment, Long> {

    Optional<PatientAppointment> findByAppointmentId(Long appointmentId);

    List<PatientAppointment> findByPatientEmailOrderByDateDesc(String patientEmail);
}
