package id.ac.ui.cs.advprog.pandacare.model;

import id.ac.ui.cs.advprog.pandacare.enums.ScheduleStatus;
import id.ac.ui.cs.advprog.pandacare.state.AvailableState;
import id.ac.ui.cs.advprog.pandacare.state.BookedState;
import id.ac.ui.cs.advprog.pandacare.state.CanceledState;
import id.ac.ui.cs.advprog.pandacare.state.CompletedState;
import java.time.DayOfWeek;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void shouldRemoveConsultationSuccessfully() {
        // Create entities for the test.
        Doctor doctor = new Doctor();
        Schedule schedule = new Schedule(doctor, DayOfWeek.MONDAY,
            LocalTime.of(9, 0), LocalTime.of(17, 0),
            ScheduleStatus.BOOKED);
        Patient patient = new Patient();
        LocalTime scheduledTime = LocalTime.of(14, 10, 0);
        Consultation consultation = new Consultation(doctor, patient, schedule,
                scheduledTime, DayOfWeek.MONDAY, "http://meeting.url", "Initial notes");

        // First, add consultation to the schedule.
        schedule.addConsultation(consultation);
        // Now remove it using removeConsultation.
        schedule.removeConsultation(consultation);

        // Verify that removal was successful.
        assertThat(schedule.getConsultation()).isNull();
        assertThat(consultation.getSchedule()).isNull();
        // After removal, the schedule should update to available.
        assertEquals(ScheduleStatus.AVAILABLE, schedule.getStatus());
        assertThat(schedule.getState()).isInstanceOf(AvailableState.class);
    }
    
    @Test
    void shouldThrowWhenRemovingConsultationWhenNoneIsSet() {
        Doctor doctor = new Doctor();
        Schedule schedule = new Schedule(doctor, DayOfWeek.MONDAY,
            LocalTime.of(9, 0), LocalTime.of(17, 0),
            ScheduleStatus.AVAILABLE);
        Patient patient = new Patient();
        LocalTime scheduledTime = LocalTime.of(14, 10, 0);
        Consultation consultation = new Consultation(doctor, patient, schedule,
                scheduledTime, DayOfWeek.MONDAY, "http://meeting.url", "Initial notes");

        // No consultation added to the schedule.
        assertThatThrownBy(() -> schedule.removeConsultation(consultation))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("The provided consultation does not match the current consultation");
    }
    
    @Test
    void shouldThrowWhenRemovingMismatchedConsultation() {
        Doctor doctor = new Doctor();
        Schedule schedule = new Schedule(doctor, DayOfWeek.MONDAY,
            LocalTime.of(9, 0), LocalTime.of(17, 0),
            ScheduleStatus.BOOKED);
        Patient patient = new Patient();
        LocalTime scheduledTime = LocalTime.of(14, 10, 0);
        Consultation consultation = new Consultation(doctor, patient, schedule,
                scheduledTime, DayOfWeek.MONDAY, "http://meeting.url", "Initial notes");
        // Add one consultation instance.
        schedule.addConsultation(consultation);

        // Create a different consultation that is not associated with the schedule.
        Consultation differentConsultation = new Consultation(doctor, patient, schedule,
                scheduledTime, DayOfWeek.MONDAY, "http://meeting.url", "Different notes");
        assertThatThrownBy(() -> schedule.removeConsultation(differentConsultation))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("The provided consultation does not match the current consultation");
    }

    @Test
    void testSetDoctorId() {
        Doctor doctor = new Doctor();
        doctor.setId(101L);

        Schedule schedule = new Schedule();
        schedule.setDoctorId(doctor);

        assertEquals(doctor, schedule.getDoctor());
    }

    @Test
    void testSetDayOfWeek() {
        Schedule schedule = new Schedule();
        schedule.setDayOfWeek(DayOfWeek.MONDAY);

        assertEquals(DayOfWeek.MONDAY, schedule.getDayOfWeek());
    }

    @Test
    void testUpdateStatus() {
        Schedule schedule = new Schedule();
        schedule.setStatus(ScheduleStatus.AVAILABLE);

        schedule.updateStatus(ScheduleStatus.BOOKED);

        assertEquals(ScheduleStatus.BOOKED, schedule.getStatus());
    }
}