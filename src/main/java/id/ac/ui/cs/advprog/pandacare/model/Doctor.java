package id.ac.ui.cs.advprog.pandacare.model;

import id.ac.ui.cs.advprog.pandacare.model.User;
import id.ac.ui.cs.advprog.pandacare.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;


@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "doctor")
public class Doctor extends User {

    @Column(name = "working_address", nullable = false)
    private String workingAddress;

    @Column(name = "specialty", nullable = false)
    private String specialty;

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", name='" + getName() + '\'' +
                ", nik='" + getNik() + '\'' +
                ", address'" + getAddress() + '\'' +
                ", workingAddress='" + workingAddress + '\'' +
                ", phoneNumber='" + getPhonenum() + '\'' +
                ", specialty='" + specialty + '\'' +
                '}';
    }

    // Constructors
    public Doctor() {
        super();
    }

    public Doctor(String email, String password, String name, String nik, String address, String workingAddress, String phonenum, Role role, String specialty) {
        super(email, password, name, nik, address, phonenum, Role.DOCTOR);
        this.workingAddress = workingAddress;
        this.specialty = specialty;
    }
}