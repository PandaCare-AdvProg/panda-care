package id.ac.ui.cs.advprog.pandacare.state;

import id.ac.ui.cs.advprog.pandacare.model.Schedule;

public interface ScheduleState {
    void book(Schedule schedule);
    void cancel(Schedule schedule);
}