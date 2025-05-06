package id.ac.ui.cs.advprog.pandacare.state;

import id.ac.ui.cs.advprog.pandacare.model.Schedule;

public class CanceledState implements ScheduleState {

    @Override
    public void book(Schedule schedule) {
        throw new IllegalStateException("Cannot book a canceled schedule");
    }

    @Override
    public void cancel(Schedule schedule) {
        throw new IllegalStateException("Schedule is already canceled");
    }
}