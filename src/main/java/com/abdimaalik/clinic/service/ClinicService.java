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

    public Appointment createAppointment(AppointmentDTO request) {
        Appointment appointment = new Appointment();
        appointment.setPatientId(request.getPatientId());
        appointment.setClinicianId(request.getClinicianId());
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getEndTime());

        validateAppointment(appointment);
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

    private void validateAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment must not be null.");
        }

        if (appointment.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID is required.");
        }

        if (appointment.getClinicianId() == null) {
            throw new IllegalArgumentException("Clinician ID is required.");
        }

        if (appointment.getStartTime() == null || appointment.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time are required.");
        }

        if (!appointment.getEndTime().isAfter(appointment.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time.");
        }
    }

    private void ensureNoOverlap(Appointment newAppointment) {
        for (Appointment existingAppointment : appointments.values()) {
            boolean sameClinician =
                    existingAppointment.getClinicianId().equals(newAppointment.getClinicianId());

            boolean overlaps =
                    newAppointment.getStartTime().isBefore(existingAppointment.getEndTime()) &&
                    newAppointment.getEndTime().isAfter(existingAppointment.getStartTime());

            if (sameClinician && overlaps) {
                throw new IllegalArgumentException("Clinician is already booked during that time.");
            }
        }
    }
}