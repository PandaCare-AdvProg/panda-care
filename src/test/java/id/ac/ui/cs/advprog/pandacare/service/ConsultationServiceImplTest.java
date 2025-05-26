package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.enums.ConsultationStatus;
import id.ac.ui.cs.advprog.pandacare.enums.ScheduleStatus;
import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import id.ac.ui.cs.advprog.pandacare.model.Schedule;
import id.ac.ui.cs.advprog.pandacare.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.pandacare.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ConsultationServiceImplTest {

    @Mock
    private ConsultationRepository consultationRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private id.ac.ui.cs.advprog.pandacare.repository.PatientRepository patientRepository;

    @InjectMocks
    private ConsultationServiceImpl consultationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        org.springframework.security.core.context.SecurityContext securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        org.springframework.security.core.Authentication authentication = mock(org.springframework.security.core.Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testCreateConsultation() {
        Consultation consultation = new Consultation();
        // Mock patient lookup by email
        id.ac.ui.cs.advprog.pandacare.model.Patient mockPatient = new id.ac.ui.cs.advprog.pandacare.model.Patient();
        when(patientRepository.findPatientByEmail("testUser")).thenReturn(java.util.Optional.of(mockPatient));
        when(consultationRepository.save(any(Consultation.class))).thenReturn(consultation);

        Consultation result = consultationService.createConsultation(consultation);

        assertNotNull(result);
        verify(consultationRepository).save(consultation);
    }

    @Test
    void testGetConsultationsByDoctor() {
        List<Consultation> consultations = List.of(new Consultation());
        when(consultationRepository.findByDoctorId(1L)).thenReturn(consultations);

        List<Consultation> result = consultationService.getConsultationsByDoctor(1L);

        assertEquals(1, result.size());
        verify(consultationRepository).findByDoctorId(1L);
    }

    @Test
    void testGetConsultationsByPatient() {
        List<Consultation> consultations = List.of(new Consultation());
        when(consultationRepository.findByPatientId(2L)).thenReturn(consultations);

        List<Consultation> result = consultationService.getConsultationsByPatient(2L);

        assertEquals(1, result.size());
        verify(consultationRepository).findByPatientId(2L);
    }

    @Test
    void testApproveConsultation() {
        Schedule schedule = new Schedule();
        Consultation consultation = new Consultation();
        consultation.setId(1L);
        consultation.setStatus(ConsultationStatus.PENDING);
        consultation.setSchedule(schedule);

        when(consultationRepository.findById(1L)).thenReturn(Optional.of(consultation));
        when(scheduleRepository.save(schedule)).thenReturn(schedule);
        when(consultationRepository.save(consultation)).thenReturn(consultation);

        Consultation result = consultationService.approveConsultation(1L);

        assertEquals(ConsultationStatus.APPROVED, result.getStatus());
        verify(scheduleRepository).save(schedule);
        verify(consultationRepository).save(consultation);
    }

    @Test
    void testRejectConsultation() {
        Schedule schedule = new Schedule();
        Consultation consultation = new Consultation();
        consultation.setId(1L);
        consultation.setSchedule(schedule);

        when(consultationRepository.findById(1L)).thenReturn(Optional.of(consultation));
        when(scheduleRepository.save(schedule)).thenReturn(schedule);

        Consultation result = consultationService.rejectConsultation(1L);

        verify(scheduleRepository, atLeastOnce()).save(schedule);
        verify(consultationRepository).delete(consultation);
        assertEquals(consultation, result);
    }

    @Test
    void testCompleteConsultation() {
        Schedule schedule = new Schedule();
        Consultation consultation = new Consultation();
        consultation.setId(1L);
        consultation.setSchedule(schedule);

        when(consultationRepository.findById(1L)).thenReturn(Optional.of(consultation));
        when(scheduleRepository.save(schedule)).thenReturn(schedule);
        when(consultationRepository.save(consultation)).thenReturn(consultation);

        Consultation result = consultationService.completeConsultation(1L);

        assertEquals(ConsultationStatus.COMPLETED, result.getStatus());
        verify(scheduleRepository).save(schedule);
        verify(consultationRepository).save(consultation);
    }

    @Test
    void testUpdateConsultation_DoctorProposesNewSlot() {
        Schedule oldSchedule = new Schedule();
        oldSchedule.setId(1L);
        oldSchedule.setStatus(ScheduleStatus.BOOKED);

        Schedule newSchedule = new Schedule();
        newSchedule.setId(2L);
        newSchedule.setStatus(ScheduleStatus.AVAILABLE);
        newSchedule.setDayOfWeek(DayOfWeek.MONDAY);
        newSchedule.setStartTime(LocalTime.of(10, 0));

        Consultation existing = new Consultation();
        existing.setId(1L);
        existing.setStatus(ConsultationStatus.PENDING);
        existing.setSchedule(oldSchedule);

        Consultation updated = new Consultation();
        updated.setStatus(ConsultationStatus.PENDING);
        updated.setSchedule(newSchedule);

        when(consultationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(scheduleRepository.findById(2L)).thenReturn(Optional.of(newSchedule));
        when(consultationRepository.save(any(Consultation.class))).thenReturn(existing);

        Consultation result = consultationService.updateConsultation(1L, updated);

        assertEquals(newSchedule, result.getSchedule());
        assertEquals(newSchedule.getDayOfWeek(), result.getDayOfWeek());
        assertEquals(newSchedule.getStartTime(), result.getScheduledTime());
        verify(scheduleRepository).saveAll(anyList());
        verify(consultationRepository).save(existing);
    }

    @Test
    void testUpdateConsultation_PatientApprovesConsultation() {
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setStatus(ScheduleStatus.PENDING);

        Consultation existing = new Consultation();
        existing.setId(1L);
        existing.setStatus(ConsultationStatus.PENDING);
        existing.setSchedule(schedule);

        Consultation updated = new Consultation();
        updated.setStatus(ConsultationStatus.APPROVED);
        updated.setSchedule(schedule);

        when(consultationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(scheduleRepository.save(schedule)).thenReturn(schedule);
        when(consultationRepository.save(existing)).thenReturn(existing);

        Consultation result = consultationService.updateConsultation(1L, updated);

        assertEquals(ConsultationStatus.APPROVED, result.getStatus());
        assertEquals(ScheduleStatus.BOOKED, schedule.getStatus());
        verify(scheduleRepository).save(schedule);
        verify(consultationRepository).save(existing);
    }

    @Test
    void testUpdateConsultation_PatientRejectsConsultation() {
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setStatus(ScheduleStatus.PENDING);

        Consultation existing = new Consultation();
        existing.setId(1L);
        existing.setStatus(ConsultationStatus.PENDING);
        existing.setSchedule(schedule);

        Consultation updated = new Consultation();
        updated.setStatus(ConsultationStatus.REJECTED);
        updated.setSchedule(schedule);

        when(consultationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(scheduleRepository.save(schedule)).thenReturn(schedule);

        Consultation result = consultationService.updateConsultation(1L, updated);

        assertNull(result);
        verify(scheduleRepository).save(schedule);
        verify(consultationRepository, atLeastOnce()).flush();
        verify(consultationRepository).delete(existing);
    }

    @Test
    void testUpdateConsultation_UpdateNotesAndMeetingUrl() {
        Schedule schedule = new Schedule();
        schedule.setId(1L);

        Consultation existing = new Consultation();
        existing.setId(1L);
        existing.setStatus(ConsultationStatus.PENDING);
        existing.setSchedule(schedule);

        Consultation updated = new Consultation();
        updated.setStatus(ConsultationStatus.PENDING);
        updated.setSchedule(schedule);
        updated.setNotes("New notes");
        updated.setMeetingUrl("http://meeting.url");

        when(consultationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(consultationRepository.save(existing)).thenReturn(existing);

        Consultation result = consultationService.updateConsultation(1L, updated);

        assertEquals("New notes", result.getNotes());
        assertEquals("http://meeting.url", result.getMeetingUrl());
        verify(consultationRepository).save(existing);
    }
    @Test
    void testCreateConsultation_ScheduleNotFound() {
        Consultation consultation = new Consultation();
        Schedule dummySchedule = new Schedule();
        dummySchedule.setId(999L);
        consultation.setSchedule(dummySchedule);
        when(scheduleRepository.findById(999L)).thenReturn(Optional.empty());
        when(patientRepository.findPatientByEmail("testUser"))
            .thenReturn(Optional.of(new Patient()));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> consultationService.createConsultation(consultation));
        assertEquals("Schedule not found", exception.getMessage());
    }
        @Test
    void testUpdateConsultation_DoctorProposesNewSlotWaitingForPatientConfirmation_ScheduleNotFound() {
        Schedule oldSchedule = new Schedule();
        oldSchedule.setId(1L);
        oldSchedule.setStatus(ScheduleStatus.BOOKED);
        oldSchedule.setDayOfWeek(DayOfWeek.MONDAY);
        oldSchedule.setStartTime(LocalTime.of(9, 0));

        Consultation existing = new Consultation();
        existing.setId(1L);
        existing.setStatus(ConsultationStatus.PENDING);
        existing.setSchedule(oldSchedule);
        
        Schedule updatedSchedule = new Schedule();
        updatedSchedule.setId(2L);
        updatedSchedule.setDayOfWeek(DayOfWeek.TUESDAY);
        updatedSchedule.setStartTime(LocalTime.of(10, 0));
        Consultation updated = new Consultation();
        updated.setStatus(ConsultationStatus.WAITING_FOR_PATIENT_CONFIRMATION);
        updated.setSchedule(updatedSchedule);
        
        when(consultationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(scheduleRepository.findById(2L)).thenReturn(Optional.empty());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> consultationService.updateConsultation(1L, updated));
        assertEquals("Schedule not found", exception.getMessage());
    }
     @Test
    void testUpdateConsultation_ConsultationNotFound() {
        when(consultationRepository.findById(999L)).thenReturn(Optional.empty());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> consultationService.updateConsultation(999L, new Consultation()));
        assertEquals("Not found: 999", exception.getMessage());
    }
    
    @Test
    void testCompleteConsultation_ConsultationNotFound() {
        when(consultationRepository.findById(999L)).thenReturn(Optional.empty());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> consultationService.completeConsultation(999L));
        assertEquals("Consultation not found with id: 999", exception.getMessage());
    }

}
