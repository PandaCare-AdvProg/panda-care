package id.ac.ui.cs.advprog.pandacare.state;

import id.ac.ui.cs.advprog.pandacare.model.Schedule;

public class CompletedState implements ScheduleState {

    @Override
    public void book(Schedule schedule) {
        throw new IllegalStateException("Cannot book a completed schedule");
    }

    @Override
    public void cancel(Schedule schedule) {
        throw new IllegalStateException("Cannot cancel a completed schedule");
    }
}