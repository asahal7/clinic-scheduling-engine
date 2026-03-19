package com.abdimaalik.clinic.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdimaalik.clinic.domain.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    boolean existsByClinicianNameAndStartTimeLessThanAndEndTimeGreaterThan(
            String clinicianName,
            LocalDateTime newEnd,
            LocalDateTime newStart
    );
}