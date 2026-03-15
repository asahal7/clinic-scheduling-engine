package com.abdimaalik.clinic.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.abdimaalik.clinic.domain.Appointment;
import com.abdimaalik.clinic.dto.AppointmentDTO;

@Service
public class ClinicService {

    private final Map<UUID, Appointment> appointments = new HashMap<>();

    public Appointment createAppointment(AppointmentDTO dto) {
        validateAppointment(dto);

        Appointment appointment = new Appointment(
                dto.getPatientId(),
                dto.getClinicianId(),
                dto.getStartTime(),
                dto.getEndTime()
        );

        ensureNoOverlap(appointment);

        UUID id = UUID.randomUUID();
        appointment.setId(id);

        appointments.put(id, appointment);
        return appointment;
    }

    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments.values());
    }

    public Optional<Appointment> getAppointment(UUID id) {
        return Optional.ofNullable(appointments.get(id));
    }

    public void cancelAppointment(UUID id) {
        appointments.remove(id);
    }

    private void validateAppointment(AppointmentDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Appointment data must not be null.");
        }

        if (dto.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID must not be null.");
        }

        if (dto.getClinicianId() == null) {
            throw new IllegalArgumentException("Clinician ID must not be null.");
        }

        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time must not be null.");
        }

        if (!dto.getEndTime().isAfter(dto.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time.");
        }
    }

    private void ensureNoOverlap(Appointment newAppointment) {
        for (Appointment existing : appointments.values()) {
            boolean sameClinician = existing.getClinicianId().equals(newAppointment.getClinicianId());

            boolean overlaps =
                    newAppointment.getStartTime().isBefore(existing.getEndTime()) &&
                    newAppointment.getEndTime().isAfter(existing.getStartTime());

            if (sameClinician && overlaps) {
                throw new IllegalArgumentException("Clinician already has an overlapping appointment.");
            }
        }
    }
}