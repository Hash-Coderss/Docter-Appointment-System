package com.serviceregistry.doctorservice.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "doctor_availability",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_doctor_availability_doctor_date", columnNames = {"doctor_id", "date"})
        }
)
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "doctor_availability_time_slots",
            joinColumns = @JoinColumn(name = "availability_id", nullable = false)
    )
    @Column(name = "time_slot", nullable = false)
    private List<String> timeSlots = new ArrayList<>();

    public DoctorAvailability() {
    }

    public DoctorAvailability(Long doctorId, LocalDate date, List<String> timeSlots) {
        this.doctorId = doctorId;
        this.date = date;
        this.timeSlots = timeSlots == null ? new ArrayList<>() : new ArrayList<>(timeSlots);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<String> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<String> timeSlots) {
        this.timeSlots = timeSlots == null ? new ArrayList<>() : new ArrayList<>(timeSlots);
    }
}
