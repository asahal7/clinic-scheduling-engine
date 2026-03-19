package com.abdimaalik.clinic.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.abdimaalik.clinic.domain.Appointment;
import com.abdimaalik.clinic.domain.AppointmentStatus;
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
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAppointment(UUID appointmentId) {
        Appointment appointment = getAppointmentOrThrow(appointmentId);

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Completed appointments cannot be cancelled.");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Appointment is already cancelled.");
        }

        if (appointment.getStatus() == AppointmentStatus.NO_SHOW) {
            throw new IllegalStateException("No-show appointments cannot be cancelled.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }

    public Appointment completeAppointment(UUID appointmentId) {
        Appointment appointment = getAppointmentOrThrow(appointmentId);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled appointments cannot be completed.");
        }

        if (appointment.getStatus() == AppointmentStatus.NO_SHOW) {
            throw new IllegalStateException("No-show appointments cannot be completed.");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Appointment is already completed.");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        return appointmentRepository.save(appointment);
    }

    public Appointment markNoShow(UUID appointmentId) {
        Appointment appointment = getAppointmentOrThrow(appointmentId);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled appointments cannot be marked as no-show.");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Completed appointments cannot be marked as no-show.");
        }

        if (appointment.getStatus() == AppointmentStatus.NO_SHOW) {
            throw new IllegalStateException("Appointment is already marked as no-show.");
        }

        appointment.setStatus(AppointmentStatus.NO_SHOW);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    private Appointment getAppointmentOrThrow(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appointmentId));
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
                appointmentRepository.existsByClinicianNameAndStartTimeLessThanAndEndTimeGreaterThanAndStatusNot(
                        dto.getClinicianName().trim(),
                        dto.getEndTime(),
                        dto.getStartTime(),
                        AppointmentStatus.CANCELLED
                );

        if (overlaps) {
            throw new IllegalArgumentException(
                    "Appointment overlaps with an existing active appointment for this clinician."
            );
        }
    }
}