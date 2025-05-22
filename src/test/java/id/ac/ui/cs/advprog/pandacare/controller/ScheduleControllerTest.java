package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScheduleControllerTest {

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    private Schedule schedule;
    private Consultation consultation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        schedule = new Schedule();
        schedule.setId(1L);
        consultation = new Consultation();
        consultation.setId(2L);
    }

    @Test
    void testCreateSchedule() {
        when(scheduleService.createSchedule(any(Schedule.class))).thenReturn(schedule);

        ResponseEntity<Schedule> response = scheduleController.createSchedule(schedule);

        assertEquals(schedule, response.getBody());
        verify(scheduleService).createSchedule(schedule);
    }

    @Test
    void testGetSchedulesByDoctor() {
        List<Schedule> schedules = Collections.singletonList(schedule);
        when(scheduleService.getSchedulesByDoctor(1L)).thenReturn(schedules);

        ResponseEntity<?> response = scheduleController.getSchedulesByDoctor(1L);

        assertEquals(schedules, response.getBody());
        verify(scheduleService).getSchedulesByDoctor(1L);
    }

    @Test
    void testAddConsultation() {
        when(scheduleService.addConsultationToSchedule(1L, consultation)).thenReturn(schedule);

        ResponseEntity<Schedule> response = scheduleController.addConsultation(1L, consultation);

        assertEquals(schedule, response.getBody());
        verify(scheduleService).addConsultationToSchedule(1L, consultation);
    }
}