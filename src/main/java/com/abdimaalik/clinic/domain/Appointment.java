package com.abdimaalik.clinic.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment {

    private UUID id;
    private UUID patientId;
    private UUID clinicianId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Appointment() {
    }

    public Appointment(UUID patientId, UUID clinicianId, LocalDateTime startTime, LocalDateTime endTime) {
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public UUID getClinicianId() {
        return clinicianId;
    }

    public void setClinicianId(UUID clinicianId) {
        this.clinicianId = clinicianId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}