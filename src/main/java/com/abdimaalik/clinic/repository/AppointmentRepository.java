package com.abdimaalik.clinic.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdimaalik.clinic.domain.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
}
