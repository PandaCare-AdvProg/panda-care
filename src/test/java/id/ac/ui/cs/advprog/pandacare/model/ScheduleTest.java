package id.ac.ui.cs.advprog.pandacare.model;
import id.ac.ui.cs.advprog.pandacare.enums.ScheduleStatus;
import id.ac.ui.cs.advprog.pandacare.state.AvailableState;
import id.ac.ui.cs.advprog.pandacare.state.BookedState;
import id.ac.ui.cs.advprog.pandacare.state.CanceledState;
import id.ac.ui.cs.advprog.pandacare.state.CompletedState;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ScheduleTest {
    
    @Test
    void scheduleCreation_shouldSetDefaultAvailable() {
        Doctor doctor = new Doctor();
        Schedule schedule = new Schedule(doctor, DayOfWeek.MONDAY, 
            LocalTime.of(9, 0), LocalTime.of(17, 0), 
            ScheduleStatus.AVAILABLE);
        
        assertThat(schedule.getStatus()).isEqualTo(ScheduleStatus.AVAILABLE);
    }

    @Test
    void shouldThrowWhenInvalidTimeRange() {
        Doctor doctor = new Doctor();
        assertThatThrownBy(() -> 
            new Schedule(doctor, DayOfWeek.MONDAY, 
                LocalTime.of(17, 0), LocalTime.of(9, 0), 
                ScheduleStatus.AVAILABLE))
            .isInstanceOf(IllegalArgumentException.class);
    }

@Test
void shouldTransitionToBookedStateWhenBookIsCalled() {
    Doctor doctor = new Doctor();
    Schedule schedule = new Schedule(doctor, DayOfWeek.MONDAY, 
        LocalTime.of(9, 0), LocalTime.of(17, 0), 
        ScheduleStatus.AVAILABLE);

    schedule.book();

    assertThat(schedule.getState()).isInstanceOf(BookedState.class);
}

@Test
void shouldThrowWhenCompleteCalledOnNonBookedState() {
    Doctor doctor = new Doctor();
    Schedule schedule = new Schedule(doctor, DayOfWeek.MONDAY, 
        LocalTime.of(9, 0), LocalTime.of(17, 0), 
        ScheduleStatus.AVAILABLE);

    assertThatThrownBy(schedule::complete)
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Only booked schedules can be completed");
}

@Test
void shouldTransitionToCompletedStateWhenCompleteIsCalledOnBookedState() {
    Doctor doctor = new Doctor();
    Schedule schedule = new Schedule(doctor, DayOfWeek.MONDAY, 
        LocalTime.of(9, 0), LocalTime.of(17, 0), 
        ScheduleStatus.BOOKED);

    schedule.complete();

    assertThat(schedule.getStatus()).isEqualTo(ScheduleStatus.COMPLETED);
    assertThat(schedule.getState()).isInstanceOf(CompletedState.class);
}

@Test
void shouldInitializeStateBasedOnStatus() {
    Doctor doctor = new Doctor();
    Schedule availableSchedule = new Schedule(doctor, DayOfWeek.MONDAY, 
        LocalTime.of(9, 0), LocalTime.of(17, 0), 
        ScheduleStatus.AVAILABLE);
    Schedule bookedSchedule = new Schedule(doctor, DayOfWeek.TUESDAY, 
        LocalTime.of(9, 0), LocalTime.of(17, 0), 
        ScheduleStatus.BOOKED);
    Schedule completedSchedule = new Schedule(doctor, DayOfWeek.WEDNESDAY, 
        LocalTime.of(9, 0), LocalTime.of(17, 0), 
        ScheduleStatus.COMPLETED);
    Schedule canceledSchedule = new Schedule(doctor, DayOfWeek.THURSDAY, 
        LocalTime.of(9, 0), LocalTime.of(17, 0), 
        ScheduleStatus.CANCELED);

    assertThat(availableSchedule.getState()).isInstanceOf(AvailableState.class);
    assertThat(bookedSchedule.getState()).isInstanceOf(BookedState.class);
    assertThat(completedSchedule.getState()).isInstanceOf(CompletedState.class);
    assertThat(canceledSchedule.getState()).isInstanceOf(CanceledState.class);
}
@Test
void shouldAddConsultationWithinTimeRange() {
    Doctor doctor = new Doctor();
    Schedule schedule = new Schedule(doctor, DayOfWeek.MONDAY, 
        LocalTime.of(9, 0), LocalTime.of(17, 0), 
        ScheduleStatus.AVAILABLE);
    Patient patient = new Patient();
    LocalDateTime scheduledTime = LocalDateTime.of(2025, 4, 14, 10, 0);
    Consultation consultation = new Consultation(doctor, patient, schedule, scheduledTime, "http://meeting.url", "Initial notes");

    schedule.addConsultation(consultation);

    assertThat(schedule.getConsultations()).contains(consultation);
    assertThat(schedule.getStatus()).isEqualTo(ScheduleStatus.BOOKED);
    assertThat(schedule.getState()).isInstanceOf(BookedState.class);
}

@Test
void shouldThrowWhenAddingConsultationOutsideTimeRange() {
    Doctor doctor = new Doctor();
    Schedule schedule = new Schedule(doctor, DayOfWeek.MONDAY, 
        LocalTime.of(9, 0), LocalTime.of(17, 0), 
        ScheduleStatus.AVAILABLE);
    Patient patient = new Patient();
    LocalDateTime scheduledTime = LocalDateTime.of(2025, 4, 14, 18, 0); // Outside time range
    Consultation consultation = new Consultation(doctor, patient, schedule, scheduledTime, "http://meeting.url", "Initial notes");

    assertThatThrownBy(() -> schedule.addConsultation(consultation))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Consultation time must be within the schedule's time range");
}

@Test
void shouldRemoveConsultationAndRevertToAvailableState() {
    Doctor doctor = new Doctor();
    Schedule schedule = new Schedule(doctor, DayOfWeek.MONDAY, 
        LocalTime.of(9, 0), LocalTime.of(17, 0), 
        ScheduleStatus.AVAILABLE);
    Patient patient = new Patient();
    LocalDateTime scheduledTime = LocalDateTime.of(2025, 4, 14, 10, 0);
    Consultation consultation = new Consultation(doctor, patient, schedule, scheduledTime, "http://meeting.url", "Initial notes");

    schedule.addConsultation(consultation);
    schedule.removeConsultation(consultation);

    assertThat(schedule.getConsultations()).doesNotContain(consultation);
    assertThat(schedule.getStatus()).isEqualTo(ScheduleStatus.AVAILABLE);
    assertThat(schedule.getState()).isInstanceOf(AvailableState.class);
}
}