CREATE TABLE appointments (
    id UUID PRIMARY KEY,
    patient_name VARCHAR(255) NOT NULL,
    clinician_name VARCHAR(255) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL
);