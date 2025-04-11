package id.ac.ui.cs.advprog.pandacare.state;

import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import id.ac.ui.cs.advprog.pandacare.enums.ScheduleStatus;

public class AvailableState implements ScheduleState {
    @Override
    public void book(Schedule schedule) {
        schedule.setStatus(ScheduleStatus.BOOKED);
        schedule.setState(new BookedState());
    }

    @Override
    public void cancel(Schedule schedule) {
        throw new IllegalStateException("Cannot cancel an available schedule");
    }
}