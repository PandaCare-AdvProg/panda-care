package id.ac.ui.cs.advprog.pandacare.model;

import id.ac.ui.cs.advprog.pandacare.enums.ScheduleStatus;
import id.ac.ui.cs.advprog.pandacare.state.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Getter
@Setter
@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "DayOfWeek", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ScheduleStatus status = ScheduleStatus.AVAILABLE;

    @jakarta.persistence.Transient
    @JsonIgnore
    private ScheduleState state = new AvailableState();

    @OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Consultation consultation;

    public void setState(ScheduleState state) {
        this.state = state;
    }

    public void book() {
        state.book(this);
    }

    public void cancel() {
        state.cancel(this);
    }

    public void complete() {
        if (!(state instanceof BookedState)) {
            throw new IllegalStateException("Only booked schedules can be completed");
        }
        this.status = ScheduleStatus.COMPLETED;
        this.state = new CompletedState();
    }

    public void addConsultation(Consultation consultation) {
        if (this.consultation != null) {
            throw new IllegalStateException("Schedule is already booked");
        }
        if (this.getStatus() == ScheduleStatus.AVAILABLE) {
            this.book();
        }
        this.consultation = consultation;
        consultation.setSchedule(this);
    }
    
    public void setDoctorId(Doctor doctorId) {
        this.doctor = doctorId;
    }
    
    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    public void updateStatus(ScheduleStatus newStatus) {
        this.status = newStatus;
        this.state = determineInitialState(newStatus);
    }
    
    public Schedule() {}

    public Schedule(Doctor doctor, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, ScheduleStatus status) {
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        this.doctor = doctor;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.state = determineInitialState(status);
    }

    private ScheduleState determineInitialState(ScheduleStatus status) {
        switch (status) {
            case AVAILABLE:
                return new AvailableState();
            case BOOKED:
                return new BookedState();
            case COMPLETED:
                return new CompletedState();
            case CANCELED:
                return new CanceledState();
            default:
                throw new IllegalArgumentException("Unknown schedule status: " + status);
        }
    }

    public void removeConsultation(Consultation existing) {
        if (this.consultation == null || !this.consultation.equals(existing)) {
            throw new IllegalStateException("The provided consultation does not match the current consultation");
        }
        this.consultation.setSchedule(null);
        this.consultation = null;
        updateStatus(ScheduleStatus.AVAILABLE);
    }
}