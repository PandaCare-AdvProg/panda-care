package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.model.Patient;
import id.ac.ui.cs.advprog.pandacare.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientControllerTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientController patientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Set up a dummy SecurityContext with a test email.
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetCurrentPatient_Found() {
        // Arrange
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setEmail("test@example.com");
        when(patientRepository.findPatientByEmail("test@example.com")).thenReturn(Optional.of(patient));
        
        // Act
        ResponseEntity<Patient> response = patientController.getCurrentPatient();
        
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.hasBody());
        assertEquals(patient, response.getBody());
    }

    @Test
    void testGetCurrentPatient_NotFound() {
        // Arrange
        when(patientRepository.findPatientByEmail("test@example.com")).thenReturn(Optional.empty());
        
        // Act
        ResponseEntity<Patient> response = patientController.getCurrentPatient();
        
        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.hasBody());
    }
}