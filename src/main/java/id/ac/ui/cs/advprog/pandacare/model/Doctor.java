package id.ac.ui.cs.advprog.pandacare.model;

import java.util.ArrayList;

import id.ac.ui.cs.advprog.pandacare.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@Entity

@Table(name = "doctor")
public class Doctor extends User {

    @Column(name = "working_address", nullable = true)
    private String workingAddress;

    @Column(name = "specialty", nullable = true, columnDefinition = "text")
    private String specialty;

    @OneToMany(
    mappedBy = "doctor",
    cascade = CascadeType.ALL,
    orphanRemoval = true
    )
    
    @JsonIgnore
    private List<Schedule> schedules = new ArrayList<>();

    public void addSchedule(Schedule sched) {
        schedules.add(sched);
        sched.setDoctor(this);
    }

    public void removeSchedule(Schedule sched) {
        schedules.remove(sched);
        sched.setDoctor(null);
    }

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

    public Doctor() {
        super();
    }

    public Doctor(String email, String password, String name, String nik, String address, String workingAddress, String phonenum, Role role, String specialty) {
        super(email, password, name, nik, address, phonenum, Role.DOCTOR);
        this.workingAddress = workingAddress;
        this.specialty = specialty;
    }
}