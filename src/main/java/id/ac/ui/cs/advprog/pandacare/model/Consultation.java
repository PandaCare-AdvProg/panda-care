package id.ac.ui.cs.advprog.pandacare.model;

import id.ac.ui.cs.advprog.pandacare.enums.ConsultationStatus;
import id.ac.ui.cs.advprog.pandacare.observer.ConsultationObserver;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @OneToOne
    @JoinColumn(name = "schedule_id", referencedColumnName = "id", nullable = false)
    private Schedule schedule;

    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;

    @Column(name = "meeting_url")
    private String meetingUrl;

    @Column(name = "notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConsultationStatus status = ConsultationStatus.PENDING;

    @jakarta.persistence.Transient
    private final List<ConsultationObserver> observers = new ArrayList<>();

    public Consultation() {}

    public Consultation(Doctor doctor, Patient patient, Schedule schedule, LocalDateTime scheduledTime, String meetingUrl, String notes) {
        this.doctor = doctor;
        this.patient = patient;
        this.schedule = schedule;
        this.scheduledTime = scheduledTime;
        this.meetingUrl = meetingUrl;
        this.notes = notes;
        this.status = ConsultationStatus.PENDING;
    }

    public void addObserver(ConsultationObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(ConsultationObserver observer) {
        observers.remove(observer);
    }

    public void setStatus(ConsultationStatus status) {
        this.status = status;
        notifyObservers("Status changed to " + status);
    }

    private void notifyObservers(String message) {
        for (ConsultationObserver observer : new ArrayList<>(observers)) {
            observer.update(this, message);
        }
    }
    
}