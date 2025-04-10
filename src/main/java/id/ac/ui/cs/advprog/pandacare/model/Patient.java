package id.ac.ui.cs.advprog.pandacare.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nik", nullable = false, unique = true)
    private String nik;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", nik='" + nik + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", medicalHistory='" + medicalHistory + '\'' +
                '}';
    }

    // Constructors
    public Patient() {
    }

    public Patient(String email, String password, String name, String nik, String address, String phoneNumber, String medicalHistory) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nik = nik;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.medicalHistory = medicalHistory;
    }
}