package id.ac.ui.cs.advprog.pandacare.state;

import id.ac.ui.cs.advprog.pandacare.enums.ScheduleStatus;
import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookedStateTest {

    @Test
    void testBookThrowsException() {
        Schedule schedule = new Schedule();
        schedule.setState(new BookedState());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            schedule.getState().book(schedule);
        });

        assertEquals("Schedule is already booked", exception.getMessage());
    }

    @Test
    void testCancel() {
        Schedule schedule = new Schedule();
        schedule.setState(new BookedState());

        schedule.getState().cancel(schedule);

        assertEquals(ScheduleStatus.AVAILABLE, schedule.getStatus());
        assertTrue(schedule.getState() instanceof AvailableState);
    }
}