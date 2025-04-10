package id.ac.ui.cs.advprog.pandacare.builder;

import id.ac.ui.cs.advprog.pandacare.model.Doctor;

public class DoctorBuilder {
    private String email;
    private String password;
    private String name;
    private String nik;
    private String workingAddress;
    private String phoneNumber;
    private String specialty;

    public DoctorBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public DoctorBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public DoctorBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DoctorBuilder setNik(String nik) {
        this.nik = nik;
        return this;
    }

    public DoctorBuilder setWorkingAddress(String workingAddress) {
        this.workingAddress = workingAddress;
        return this;
    }

    public DoctorBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public DoctorBuilder setSpecialty(String specialty) {
        this.specialty = specialty;
        return this;
    }

    public Doctor build() {
        if (email == null || password == null || name == null || nik == null || workingAddress == null || phoneNumber == null) {
            throw new IllegalStateException("Required fields are missing");
        }
        return new Doctor(email, password, name, nik, workingAddress, phoneNumber, specialty);
    }
}