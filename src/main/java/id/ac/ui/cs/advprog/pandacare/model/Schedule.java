package id.ac.ui.cs.advprog.pandacare.model;
import id.ac.ui.cs.advprog.pandacare.enums.ScheduleStatus;
import id.ac.ui.cs.advprog.pandacare.state.ScheduleState;
import id.ac.ui.cs.advprog.pandacare.state.AvailableState;
import id.ac.ui.cs.advprog.pandacare.state.BookedState;
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
        this.status = ScheduleStatus.BOOKED;
        this.state = new BookedState();
    }

    public void cancel() {
        state.cancel(this);
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
    }
}