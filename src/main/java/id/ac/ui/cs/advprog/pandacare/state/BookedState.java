package id.ac.ui.cs.advprog.pandacare.state;

import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import id.ac.ui.cs.advprog.pandacare.enums.ScheduleStatus;

public class BookedState implements ScheduleState {
    @Override
    public void book(Schedule schedule) {
        throw new IllegalStateException("Schedule is already booked");
    }

    @Override
    public void cancel(Schedule schedule) {
        schedule.setStatus(ScheduleStatus.AVAILABLE);
        schedule.setState(new AvailableState());
    }
}