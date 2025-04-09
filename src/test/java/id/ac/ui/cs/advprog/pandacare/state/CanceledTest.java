package id.ac.ui.cs.advprog.pandacare.state;

import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CanceledStateTest {

    @Test
    void testBookThrowsException() {
        Schedule schedule = new Schedule();
        schedule.setState(new CanceledState());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            schedule.getState().book(schedule);
        });

        assertEquals("Cannot book a canceled schedule", exception.getMessage());
    }

    @Test
    void testCancelThrowsException() {
        Schedule schedule = new Schedule();
        schedule.setState(new CanceledState());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            schedule.getState().cancel(schedule);
        });

        assertEquals("Schedule is already canceled", exception.getMessage());
    }
}