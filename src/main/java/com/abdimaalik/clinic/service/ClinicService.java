package com.abdimaalik.clinic.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.abdimaalik.clinic.domain.Appointment;
import com.abdimaalik.clinic.repository.AppointmentRepository;

@Service
public class ClinicService {

    private final AppointmentRepository appointmentRepository;

    public ClinicService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment createAppointment(Appointment appointment) {
        validateAppointment(appointment);

        if (appointment.getId() == null) {
            appointment.setId(UUID.randomUUID());
        }

        ensureNoOverlap(appointment);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointment(UUID id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + id));
    }

    public void cancelAppointment(UUID id) {
        appointmentRepository.deleteById(id);
    }

    private void validateAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        if (appointment.getPatientName() == null || appointment.getPatientName().isBlank()) {
            throw new IllegalArgumentException("Patient name is required");
        }
        if (appointment.getClinicianName() == null || appointment.getClinicianName().isBlank()) {
            throw new IllegalArgumentException("Clinician name is required");
        }
        if (appointment.getAppointmentTime() == null) {
            throw new IllegalArgumentException("Appointment time is required");
        }
    }

    private void ensureNoOverlap(Appointment appointment) {
        // keep this simple for now
        // later we can replace this with a repository query
    }
}