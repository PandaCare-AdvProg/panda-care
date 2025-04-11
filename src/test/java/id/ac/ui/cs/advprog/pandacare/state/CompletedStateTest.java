package id.ac.ui.cs.advprog.pandacare.state;

import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompletedStateTest {

    @Test
    void testBookThrowsException() {
        Schedule schedule = new Schedule();
        schedule.setState(new CompletedState());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            schedule.getState().book(schedule);
        });

        assertEquals("Cannot book a completed schedule", exception.getMessage());
    }

    @Test
    void testCancelThrowsException() {
        Schedule schedule = new Schedule();
        schedule.setState(new CompletedState());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            schedule.getState().cancel(schedule);
        });

        assertEquals("Cannot cancel a completed schedule", exception.getMessage());
    }
}