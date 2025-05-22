package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import id.ac.ui.cs.advprog.pandacare.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.pandacare.repository.DoctorRepository;
import id.ac.ui.cs.advprog.pandacare.repository.PatientRepository;
import id.ac.ui.cs.advprog.pandacare.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ScheduleServiceImplTest {

    @Autowired
    private ScheduleServiceImpl scheduleService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private Doctor doctor;
    private Patient patient;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setEmail("doctor1@example.com");
        doctor.setSpecialty("General");
        doctor.setWorkingAddress("Clinic");
        doctor = doctorRepository.save(doctor);

        patient = new Patient();
        patient.setEmail("patient1@example.com");
        patient.setName("Test Patient");
        patient = patientRepository.save(patient);
    }

    @Test
    @DisplayName("Should create schedule for authenticated doctor")
    void testCreateSchedule() {
        // Mock authentication
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(doctor.getEmail(), "password", Collections.emptyList())
        );

        Schedule schedule = new Schedule();
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(10, 0));

        Schedule saved = scheduleService.createSchedule(schedule);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDoctor().getId()).isEqualTo(doctor.getId());
    }

    @Test
    @DisplayName("Should get schedules by doctor id")
    void testGetSchedulesByDoctor() {
        Schedule schedule = new Schedule();
        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(DayOfWeek.TUESDAY);
        schedule.setStartTime(LocalTime.of(10, 0));
        schedule.setEndTime(LocalTime.of(11, 0));
        schedule = scheduleRepository.save(schedule);

        var schedules = scheduleService.getSchedulesByDoctor(doctor.getId());

        assertThat(schedules).isNotEmpty();
        assertThat(schedules.get(0).getDoctor().getId()).isEqualTo(doctor.getId());
    }

    @Test
    @DisplayName("Should add consultation to schedule for authenticated patient")
    void testAddConsultationToSchedule() {
        // Mock authentication
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(patient.getEmail(), "password", Collections.emptyList())
        );

        Schedule schedule = new Schedule();
        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(DayOfWeek.WEDNESDAY);
        schedule.setStartTime(LocalTime.of(11, 0));
        schedule.setEndTime(LocalTime.of(12, 0));
        schedule = scheduleRepository.save(schedule);

        Consultation consultation = new Consultation();
        consultation.setNotes("Test consultation");

        Schedule updatedSchedule = scheduleService.addConsultationToSchedule(schedule.getId(), consultation);

        assertThat(updatedSchedule.getConsultation()).isNotNull();
        Consultation savedConsultation = updatedSchedule.getConsultation();
        assertThat(savedConsultation.getPatient().getId()).isEqualTo(patient.getId());
        assertThat(savedConsultation.getDoctor().getId()).isEqualTo(doctor.getId());
        assertThat(savedConsultation.getSchedule().getId()).isEqualTo(schedule.getId());
    }
}