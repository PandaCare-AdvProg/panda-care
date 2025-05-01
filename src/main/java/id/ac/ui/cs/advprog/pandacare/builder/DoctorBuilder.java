package id.ac.ui.cs.advprog.pandacare.builder;

import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.enums.Role;

public class DoctorBuilder {
    private String email;
    private String password;
    private String name;
    private String nik;
    private String address;
    private String workingAddress;
    private String phoneNumber;
    private String specialty;
    private Role role;

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

    public DoctorBuilder setAddress(String address) {
        this.address = address;
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

    public DoctorBuilder setRole(Role role) {
        this.role = role;
        return this;
    }

    public Doctor build() {
        if (email == null || password == null || name == null || nik == null || address == null || workingAddress == null || phoneNumber == null|| role == null) {
            throw new IllegalStateException("Required fields are missing");
        }
        return new Doctor(email, password, name, nik, address, workingAddress, phoneNumber, role, specialty);
    }
}