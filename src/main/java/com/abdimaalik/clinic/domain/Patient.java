package com.abdimaalik.clinic.domain;

import java.util.UUID;

public class Patient {

    private UUID id;
    private String name;
    private String email;

    public Patient() {}

    public Patient(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UUID getId() { return id; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public void setId(UUID id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setEmail(String email) { this.email = email; }
}
