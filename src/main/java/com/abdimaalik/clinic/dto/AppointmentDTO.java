package com.abdimaalik.clinic.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AppointmentDTO {

    private String patientName;
    private String clinicianName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal fee;

    public AppointmentDTO() {
    }

    public AppointmentDTO(String patientName,
                          String clinicianName,
                          LocalDateTime startTime,
                          LocalDateTime endTime,
                          BigDecimal fee) {
        this.patientName = patientName;
        this.clinicianName = clinicianName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.fee = fee;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getClinicianName() {
        return clinicianName;
    }

    public void setClinicianName(String clinicianName) {
        this.clinicianName = clinicianName;
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

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}