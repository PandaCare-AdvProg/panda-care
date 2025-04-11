package id.ac.ui.cs.advprog.pandacare.state;

import id.ac.ui.cs.advprog.pandacare.enums.ScheduleStatus;
import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AvailableStateTest {

    @Test
    void testBook() {
        Schedule schedule = new Schedule();
        schedule.setState(new AvailableState());

        schedule.getState().book(schedule);

        assertEquals(ScheduleStatus.BOOKED, schedule.getStatus());
        assertTrue(schedule.getState() instanceof BookedState);
    }

    @Test
    void testCancelThrowsException() {
        Schedule schedule = new Schedule();
        schedule.setState(new AvailableState());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            schedule.getState().cancel(schedule);
        });

        assertEquals("Cannot cancel an available schedule", exception.getMessage());
    }
}