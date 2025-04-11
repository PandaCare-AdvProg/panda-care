package id.ac.ui.cs.advprog.pandacare.model;

import id.ac.ui.cs.advprog.pandacare.Auth.User;
import id.ac.ui.cs.advprog.pandacare.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "patient")
public class Patient extends User {

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", name='" + getName() + '\'' +
                ", nik='" + getNik() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", phoneNumber='" + getPhonenum() + '\'' +
                ", medicalHistory='" + medicalHistory + '\'' +
                '}';
    }

    public Patient() {
        super();
    }

    public Patient(String email, String password, String name, String nik, String address, String phonenum, Role role, String medicalHistory) {
        super(email, password, name, nik, address, phonenum, role);
        this.medicalHistory = medicalHistory;
    }
}