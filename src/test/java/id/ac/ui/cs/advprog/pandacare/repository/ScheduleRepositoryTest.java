package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.enums.ScheduleStatus;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScheduleRepositoryTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleRepositoryTest scheduleRepositoryTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAndFindById() {
        Doctor doctor = new Doctor();
        doctor.setId(101L);

        Schedule schedule = new Schedule();
        schedule.setDoctorId(doctor);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(11, 0));

        when(scheduleRepository.save(schedule)).thenReturn(schedule);
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.of(schedule));

        Schedule savedSchedule = scheduleRepository.save(schedule);
        Optional<Schedule> found = scheduleRepository.findById(savedSchedule.getId());

        assertTrue(found.isPresent());
        assertEquals(DayOfWeek.MONDAY, found.get().getDayOfWeek());
    }

    @Test
    void testFindByDoctorId() {
        Doctor doctor = new Doctor();
        doctor.setId(102L);

        Schedule schedule = new Schedule();
        schedule.setDoctorId(doctor);
        schedule.setDayOfWeek(DayOfWeek.TUESDAY);
        schedule.setStartTime(LocalTime.of(10, 0));
        schedule.setEndTime(LocalTime.of(12, 0));

        List<Schedule> schedules = new ArrayList<>();
        schedules.add(schedule);

        when(scheduleRepository.findByDoctor_Id(102L)).thenReturn(schedules);

        List<Schedule> results = scheduleRepository.findByDoctor_Id(102L);

        assertEquals(1, results.size());
        assertEquals(DayOfWeek.TUESDAY, results.get(0).getDayOfWeek());
    }

    @Test
    void testFindByDayOfWeek() {
        Doctor doctor = new Doctor();
        doctor.setId(103L);

        Schedule schedule = new Schedule();
        schedule.setDoctorId(doctor);
        schedule.setDayOfWeek(DayOfWeek.WEDNESDAY);
        schedule.setStartTime(LocalTime.of(13, 0));
        schedule.setEndTime(LocalTime.of(15, 0));

        List<Schedule> schedules = new ArrayList<>();
        schedules.add(schedule);

        when(scheduleRepository.findByDayOfWeek("WEDNESDAY")).thenReturn(schedules);

        List<Schedule> results = scheduleRepository.findByDayOfWeek("WEDNESDAY");

        assertEquals(1, results.size());
        assertEquals(DayOfWeek.WEDNESDAY, results.get(0).getDayOfWeek());
    }

    @Test
    void testDeleteById() {
        Long scheduleId = 104L;

        doNothing().when(scheduleRepository).deleteById(scheduleId);

        scheduleRepository.deleteById(scheduleId);

        verify(scheduleRepository, times(1)).deleteById(scheduleId);
    }

    @Test
    void testUpdateStatus() {
        Doctor doctor = new Doctor();
        doctor.setId(105L);

        Schedule schedule = new Schedule();
        schedule.setDoctorId(doctor);
        schedule.setDayOfWeek(DayOfWeek.SATURDAY);
        schedule.setStartTime(LocalTime.of(8, 0));
        schedule.setEndTime(LocalTime.of(10, 0));
        schedule.setStatus(ScheduleStatus.AVAILABLE);

        when(scheduleRepository.save(schedule)).thenReturn(schedule);
        schedule.updateStatus(ScheduleStatus.BOOKED);
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.of(schedule));

        scheduleRepository.save(schedule);
        Optional<Schedule> found = scheduleRepository.findById(schedule.getId());

        assertTrue(found.isPresent());
        assertEquals(ScheduleStatus.BOOKED, found.get().getStatus());
    }
}