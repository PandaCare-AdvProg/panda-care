package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.service.ConsultationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsultationControllerTest {

    @Mock
    private ConsultationService consultationService;

    @InjectMocks
    private ConsultationController consultationController;

    private Consultation consultation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
}