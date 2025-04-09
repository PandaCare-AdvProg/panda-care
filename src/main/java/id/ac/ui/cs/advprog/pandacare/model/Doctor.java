package id.ac.ui.cs.advprog.pandacare.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;


@Getter @Setter
@EqualsAndHashCode
@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name ="nik", nullable = false, unique = true)
    private String nik;

    @Column(name = "working_address", nullable = false)
    private String workingAddress;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "specialty", nullable = false)
    private String specialty;

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", nik='" + nik + '\'' +
                ", workingAddress='" + workingAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", specialty='" + specialty + '\'' +
                '}';
    }

    // Constructors
    public Doctor() {
    }

    public Doctor(String email, String password, String name, String nik, String workingAddress, String phoneNumber, String specialty) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nik = nik;
        this.workingAddress = workingAddress;
        this.phoneNumber = phoneNumber;
        this.specialty = specialty;
    }

}