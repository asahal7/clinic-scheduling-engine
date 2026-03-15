package com.abdimaalik.clinic.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdimaalik.clinic.domain.Appointment;
import com.abdimaalik.clinic.dto.AppointmentDTO;
import com.abdimaalik.clinic.exception.NotFoundException;
import com.abdimaalik.clinic.service.ClinicService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final ClinicService clinicService;

    public AppointmentController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody AppointmentDTO dto) {
        Appointment created = clinicService.createAppointment(dto);

        return ResponseEntity
                .created(URI.create("/appointments/" + created.getId()))
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(clinicService.getAppointments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable UUID id) {
        Appointment appointment = clinicService.getAppointment(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found with id: " + id));

        return ResponseEntity.ok(appointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable UUID id) {
        Appointment appointment = clinicService.getAppointment(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found with id: " + id));

        clinicService.cancelAppointment(appointment.getId());
        return ResponseEntity.noContent().build();
    }
}

