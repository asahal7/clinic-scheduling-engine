package com.abdimaalik.clinic.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.abdimaalik.clinic.domain.Appointment;
import com.abdimaalik.clinic.dto.AppointmentDTO;
import com.abdimaalik.clinic.repository.AppointmentRepository;

@Service
public class ClinicService {

    private final AppointmentRepository appointmentRepository;

    public ClinicService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment scheduleAppointment(AppointmentDTO dto) {
        validateAppointment(dto);
        validateNoOverlap(dto);

        Appointment appointment = new Appointment();
        appointment.setId(UUID.randomUUID());
        appointment.setPatientName(dto.getPatientName().trim());
        appointment.setClinicianName(dto.getClinicianName().trim());
        appointment.setStartTime(dto.getStartTime());
        appointment.setEndTime(dto.getEndTime());
        appointment.setFee(dto.getFee());

        return appointmentRepository.save(appointment);
    }

    private void validateAppointment(AppointmentDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Appointment request must not be null.");
        }

        if (dto.getPatientName() == null || dto.getPatientName().isBlank()) {
            throw new IllegalArgumentException("Patient name must not be blank.");
        }

        if (dto.getClinicianName() == null || dto.getClinicianName().isBlank()) {
            throw new IllegalArgumentException("Clinician name must not be blank.");
        }

        if (dto.getStartTime() == null) {
            throw new IllegalArgumentException("Start time must not be null.");
        }

        if (dto.getEndTime() == null) {
            throw new IllegalArgumentException("End time must not be null.");
        }

        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        if (dto.getFee() == null) {
            throw new IllegalArgumentException("Fee must not be null.");
        }

        if (dto.getFee().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Fee must be greater than zero.");
        }
    }

    private void validateNoOverlap(AppointmentDTO dto) {
        boolean overlaps =
                appointmentRepository.existsByClinicianNameAndStartTimeLessThanAndEndTimeGreaterThan(
                        dto.getClinicianName().trim(),
                        dto.getEndTime(),
                        dto.getStartTime()
                );

        if (overlaps) {
            throw new IllegalArgumentException(
                    "Appointment overlaps with an existing appointment for this clinician."
            );
        }
    }
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}