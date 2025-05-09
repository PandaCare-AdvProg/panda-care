package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import java.util.List;

public interface ScheduleService {
    Schedule createSchedule(Schedule schedule);
    List<Schedule> getSchedulesByDoctor(Long doctorId);
    Schedule addConsultationToSchedule(Long scheduleId, Consultation consultation);
}