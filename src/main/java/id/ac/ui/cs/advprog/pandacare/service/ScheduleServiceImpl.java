package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import id.ac.ui.cs.advprog.pandacare.repository.ScheduleRepository;
import id.ac.ui.cs.advprog.pandacare.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.pandacare.repository.DoctorRepository;
import id.ac.ui.cs.advprog.pandacare.repository.PatientRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ConsultationRepository consultationRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository,
                               ConsultationRepository consultationRepository,
                               DoctorRepository doctorRepository,
                               PatientRepository patientRepository) {

        this.doctorRepository = doctorRepository;
        this.scheduleRepository = scheduleRepository;
        this.consultationRepository = consultationRepository;
        this.patientRepository = patientRepository;
    }
    
    @Override
    @Transactional
    public Schedule createSchedule(Schedule schedule) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Doctor managedDoctor = doctorRepository.findDoctorByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("Doctor not found with email: " + email));

    schedule.setDoctor(managedDoctor);
    return scheduleRepository.save(schedule);
}

    @Override
    public List<Schedule> getSchedulesByDoctor(Long doctorId) {
        return scheduleRepository.findByDoctor_Id(doctorId);
    }

@Override
@Transactional
public Schedule addConsultationToSchedule(Long scheduleId, Consultation consultation) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Patient managedPatient = patientRepository.findPatientByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("Patient not found with email: " + email));

    Optional<Schedule> optSchedule = scheduleRepository.findById(scheduleId);
    if (!optSchedule.isPresent()) {
        throw new IllegalArgumentException("Schedule not found with id: " + scheduleId);
    }

    Schedule schedule = optSchedule.get();
    consultation.setScheduledTime(schedule.getStartTime());
    consultation.setDoctor(schedule.getDoctor());
    consultation.setSchedule(schedule);
    consultation.setPatient(managedPatient);
    schedule.addConsultation(consultation);
    consultationRepository.save(consultation);
    return scheduleRepository.save(schedule);
}
}