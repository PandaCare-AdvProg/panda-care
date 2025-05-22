package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import id.ac.ui.cs.advprog.pandacare.service.ConsultationService;
import id.ac.ui.cs.advprog.pandacare.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ConsultationControllerTest {

    @Mock
    private ConsultationService consultationService;
    @Mock
    private PatientRepository patientRepository;
    @InjectMocks
    private ConsultationController consultationController;

    private Consultation consultation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up a dummy security context with "testUser" as the authenticated username.
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);

        consultation = new Consultation();
        consultation.setId(1L);
    }

    @Test
    void testCreateConsultation() {
        when(consultationService.createConsultation(any(Consultation.class))).thenReturn(consultation);

        ResponseEntity<Consultation> response = consultationController.createConsultation(consultation);

        assertEquals(consultation, response.getBody());
        verify(consultationService).createConsultation(consultation);
    }

    @Test
    void testUpdateConsultation() {
        when(consultationService.updateConsultation(eq(1L), any(Consultation.class))).thenReturn(consultation);

        ResponseEntity<Consultation> response = consultationController.updateConsultation(1L, consultation);

        assertEquals(consultation, response.getBody());
        verify(consultationService).updateConsultation(1L, consultation);
    }

    @Test
    void testApproveConsultation() {
        when(consultationService.approveConsultation(1L)).thenReturn(consultation);

        ResponseEntity<Consultation> response = consultationController.approveConsultation(1L);

        assertEquals(consultation, response.getBody());
        verify(consultationService).approveConsultation(1L);
    }

    @Test
    void testRejectConsultation() {
        when(consultationService.rejectConsultation(1L)).thenReturn(consultation);

        ResponseEntity<Consultation> response = consultationController.rejectConsultation(1L);

        assertEquals(consultation, response.getBody());
        verify(consultationService).rejectConsultation(1L);
    }

    @Test
    void testCompleteConsultation() {
        when(consultationService.completeConsultation(1L)).thenReturn(consultation);

        ResponseEntity<Consultation> response = consultationController.completeConsultation(1L);

        assertEquals(consultation, response.getBody());
        verify(consultationService).completeConsultation(1L);
    }

    @Test
    void testGetConsultationsByDoctor() {
        List<Consultation> consultations = List.of(consultation);
        when(consultationService.getConsultationsByDoctor(10L)).thenReturn(consultations);

        ResponseEntity<List<Consultation>> response = consultationController.getConsultationsByDoctor(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(consultationService).getConsultationsByDoctor(10L);
    }

    @Test
    void testGetConsultationsByPatient_Authorized() {
        // Arrange
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientRepository.findPatientByEmail("testUser")).thenReturn(Optional.of(patient));

        List<Consultation> consultations = List.of(consultation);
        when(consultationService.getConsultationsByPatient(1L)).thenReturn(consultations);

        // Act
        ResponseEntity<List<Consultation>> response = consultationController.getConsultationsByPatient(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(patientRepository).findPatientByEmail("testUser");
        verify(consultationService).getConsultationsByPatient(1L);
    }

    @Test
    void testGetConsultationsByPatient_Unauthorized() {
        // Arrange
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientRepository.findPatientByEmail("testUser")).thenReturn(Optional.of(patient));

        // Act: request consultations with a mismatched patient id
        ResponseEntity<List<Consultation>> response = consultationController.getConsultationsByPatient(2L);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
        verify(patientRepository).findPatientByEmail("testUser");
        verify(consultationService, never()).getConsultationsByPatient(anyLong());
    }
    @Test
    void testGetConsultationsByPatient_PatientNotFound() {
    // Arrange: set patientRepository to return empty.
    when(patientRepository.findPatientByEmail("testUser")).thenReturn(Optional.empty());
    
    // Act Assert: expecting an IllegalArgumentException with the proper message.
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> consultationController.getConsultationsByPatient(1L)
    );
    assertEquals("Patient not found with email: testUser", exception.getMessage());
}
}