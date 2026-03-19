package com.abdimaalik.clinic.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Appointment createAppointment(@RequestBody AppointmentDTO dto) {
        return clinicService.createAppointment(dto);
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return clinicService.getAllAppointments();
    }
}

