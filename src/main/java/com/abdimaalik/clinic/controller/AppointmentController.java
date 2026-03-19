package com.abdimaalik.clinic.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdimaalik.clinic.domain.Appointment;
import com.abdimaalik.clinic.dto.AppointmentDTO;
import com.abdimaalik.clinic.service.ClinicService;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final ClinicService clinicService;

    public AppointmentController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @PostMapping
    public Appointment scheduleAppointment(@RequestBody AppointmentDTO dto) {
        return clinicService.scheduleAppointment(dto);
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return clinicService.getAllAppointments();
    }

    @PatchMapping("/{id}/cancel")
    public Appointment cancelAppointment(@PathVariable UUID id) {
        return clinicService.cancelAppointment(id);
    }

    @PatchMapping("/{id}/complete")
    public Appointment completeAppointment(@PathVariable UUID id) {
        return clinicService.completeAppointment(id);
    }

    @PatchMapping("/{id}/no-show")
    public Appointment markNoShow(@PathVariable UUID id) {
        return clinicService.markNoShow(id);
    }
}
