package com.abdimaalik.clinic.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.abdimaalik.clinic.domain.Appointment;
import com.abdimaalik.clinic.domain.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    boolean existsByClinicianNameAndStartTimeLessThanAndEndTimeGreaterThanAndStatusNot(
            String clinicianName,
            LocalDateTime newEnd,
            LocalDateTime newStart,
            AppointmentStatus status
    );

    Page<Appointment> findByStatus(AppointmentStatus status, Pageable pageable);

    Page<Appointment> findByClinicianNameContainingIgnoreCase(String clinicianName, Pageable pageable);

    Page<Appointment> findByPatientNameContainingIgnoreCase(String patientName, Pageable pageable);

    Page<Appointment> findByClinicianNameContainingIgnoreCaseAndStatus(
            String clinicianName,
            AppointmentStatus status,
            Pageable pageable
    );

    Page<Appointment> findByPatientNameContainingIgnoreCaseAndStatus(
            String patientName,
            AppointmentStatus status,
            Pageable pageable
    );
}