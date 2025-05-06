package id.ac.ui.cs.advprog.pandacare.builder;

import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.model.Patient;

public class PatientBuilder {
    private String email;
    private String password;
    private String name;
    private String nik;
    private String address;
    private String phoneNumber;
    private String medicalHistory;
    private Role role; // Add Role field

    public PatientBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public PatientBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public PatientBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PatientBuilder setNik(String nik) {
        this.nik = nik;
        return this;
    }

    public PatientBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public PatientBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public PatientBuilder setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
        return this;
    }

    public PatientBuilder setRole(Role role) { 
        this.role = role;
        return this;
    }

    public Patient build() {
        if (email == null || password == null || name == null || nik == null || address == null || phoneNumber == null || role == null) {
            throw new IllegalStateException("Required fields are missing");
        }
        return new Patient(email, password, name, nik, address, phoneNumber, role, medicalHistory);
    }
}