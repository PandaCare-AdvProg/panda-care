package id.ac.ui.cs.advprog.pandacare.model;

import id.ac.ui.cs.advprog.pandacare.enums.ScheduleStatus;
import id.ac.ui.cs.advprog.pandacare.state.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
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
    private ScheduleState state = new AvailableState();

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

    public void markAsCanceled() {
        if (!(state instanceof AvailableState || state instanceof BookedState)) {
            throw new IllegalStateException("Only available or booked schedules can be canceled");
        }
        this.status = ScheduleStatus.CANCELED;
        this.state = new CanceledState();
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
}