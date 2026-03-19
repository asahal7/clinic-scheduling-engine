package com.abdimaalik.clinic.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.abdimaalik.clinic.domain.Appointment;
import com.abdimaalik.clinic.dto.AppointmentDTO;
import com.abdimaalik.clinic.exception.AppointmentConflictException;
import com.abdimaalik.clinic.repository.AppointmentRepository;

@Service
public class ClinicService {

    private final AppointmentRepository appointmentRepository;

    public ClinicService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment createAppointment(AppointmentDTO dto) {
        validateAppointment(dto);
        ensureNoClinicianOverlap(dto);

        Appointment appointment = new Appointment();
        appointment.setId(dto.getId() != null ? dto.getId() : UUID.randomUUID());
        appointment.setPatientName(dto.getPatientName());
        appointment.setClinicianName(dto.getClinicianName());
        appointment.setStartTime(dto.getStartTime());
        appointment.setEndTime(dto.getEndTime());

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
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
    }

    private void ensureNoClinicianOverlap(AppointmentDTO dto) {
        boolean overlapExists =
                appointmentRepository.existsByClinicianNameAndStartTimeLessThanAndEndTimeGreaterThan(
                        dto.getClinicianName(),
                        dto.getEndTime(),
                        dto.getStartTime()
                );

        if (overlapExists) {
            throw new AppointmentConflictException(
                    "Clinician already has an overlapping appointment."
            );
        }
    }
}