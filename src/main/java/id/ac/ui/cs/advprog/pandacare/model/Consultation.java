package id.ac.ui.cs.advprog.pandacare.model;
import id.ac.ui.cs.advprog.pandacare.enums.ConsultationStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "consultations")
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;

    @Column(name = "meeting_url")
    private String meetingUrl;

    @Column(name = "notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConsultationStatus status = ConsultationStatus.PENDING;


    public Consultation() {}

    public Consultation(Doctor doctor, Patient patient, LocalDateTime scheduledTime, String meetingUrl, String notes) {
        this.doctor = doctor;
        this.patient = patient;
        this.scheduledTime = scheduledTime;
        this.meetingUrl = meetingUrl;
        this.notes = notes;
        this.status = ConsultationStatus.PENDING;
    }

}