package id.ac.ui.cs.advprog.pandacare.model;
import id.ac.ui.cs.advprog.pandacare.enums.ScheduleStatus;
import java.time.DayOfWeek;
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
}