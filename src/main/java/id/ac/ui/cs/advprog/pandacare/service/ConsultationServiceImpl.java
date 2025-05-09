package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import id.ac.ui.cs.advprog.pandacare.enums.ConsultationStatus;
import id.ac.ui.cs.advprog.pandacare.enums.ScheduleStatus;
import id.ac.ui.cs.advprog.pandacare.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.pandacare.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final ScheduleRepository scheduleRepository;

    public ConsultationServiceImpl(ConsultationRepository consultationRepository,
                                   ScheduleRepository scheduleRepository) {
        this.consultationRepository = consultationRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    @Transactional
    public Consultation createConsultation(Consultation consultation) {
        return consultationRepository.save(consultation);
    }

    @Override
    @Transactional
    public Consultation updateConsultation(Long id, Consultation updated) {
        Consultation existing = consultationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Not found: " + id));
    
        // 1) Doctor proposes a new slot
        if (existing.getStatus() == ConsultationStatus.PENDING
            && updated.getStatus() == ConsultationStatus.PENDING
            && !existing.getSchedule().getId().equals(updated.getSchedule().getId())) {
    
            Schedule oldSched = existing.getSchedule();
            Schedule newSched = scheduleRepository.findById(updated.getSchedule().getId())
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
    
            // Free old slot, mark new slot as AVAILABLE or PENDING
            oldSched.setStatus(ScheduleStatus.AVAILABLE);
            newSched.setStatus(ScheduleStatus.PENDING);
            scheduleRepository.saveAll(List.of(oldSched, newSched));
    
            // Update the schedule, scheduled_time, and dayOfWeek
            existing.setSchedule(newSched);
            existing.setDayOfWeek(newSched.getDayOfWeek());
            existing.setScheduledTime(newSched.getStartTime());        
                
            existing.notifyObservers("Doctor proposed new time: " + newSched.getId());
        }
        
        else if (updated.getStatus() == ConsultationStatus.APPROVED) {
            existing.setStatus(ConsultationStatus.APPROVED);
            Schedule sched = existing.getSchedule();
            sched.setStatus(ScheduleStatus.BOOKED);
            scheduleRepository.save(sched);
            existing.notifyObservers("Patient accepted time: " + sched.getId());
        }
        else if (updated.getStatus() == ConsultationStatus.REJECTED) {
            Schedule sched = existing.getSchedule();
            sched.setStatus(ScheduleStatus.AVAILABLE);
    
            sched.setConsultation(null);
            existing.setSchedule(null);
    
            scheduleRepository.save(sched);
    
            consultationRepository.flush();
            consultationRepository.delete(existing);
            consultationRepository.flush();
    
            return null;
        }
    
        // Update other fields
        existing.setNotes(updated.getNotes());
        existing.setMeetingUrl(updated.getMeetingUrl());
    
        return consultationRepository.save(existing);
    }
    @Override
    @Transactional
    public Consultation completeConsultation(Long consultationId) {
        Optional<Consultation> optConsultation = consultationRepository.findById(consultationId);
        if (!optConsultation.isPresent()) {
            throw new IllegalArgumentException("Consultation not found with id: " + consultationId);
        }
        Consultation consultation = optConsultation.get();
        consultation.setStatus(ConsultationStatus.COMPLETED);
        
        Schedule schedule = consultation.getSchedule();
        schedule.setStatus(ScheduleStatus.COMPLETED);
        scheduleRepository.save(schedule);
        
        return consultationRepository.save(consultation);
    }

    @Override
    public List<Consultation> getConsultationsByDoctor(Long doctorId) {
        return consultationRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<Consultation> getConsultationsByPatient(Long patientId) {
        return consultationRepository.findByPatientId(patientId);
    }

    @Override
    public Consultation approveConsultation(Long consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
            .orElseThrow(() -> new IllegalArgumentException("Consultation not found"));
        consultation.setStatus(ConsultationStatus.APPROVED);
        consultation.getSchedule().setStatus(ScheduleStatus.BOOKED);
        scheduleRepository.save(consultation.getSchedule());
        return consultationRepository.save(consultation);
    }

    @Override
    @Transactional
    public Consultation rejectConsultation(Long consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
            .orElseThrow(() -> new IllegalArgumentException("Consultation not found"));
    
        // Update schedule status
        Schedule schedule = consultation.getSchedule();
        schedule.setStatus(ScheduleStatus.AVAILABLE);
        scheduleRepository.save(schedule);
        schedule.setConsultation(null);
        consultation.setSchedule(null);
        scheduleRepository.save(schedule);
        consultationRepository.save(consultation);
        consultationRepository.flush();
        consultationRepository.delete(consultation);
        consultationRepository.flush();
        return consultation;
    }

}