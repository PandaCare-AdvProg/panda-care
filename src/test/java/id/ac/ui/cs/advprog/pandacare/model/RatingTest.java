package id.ac.ui.cs.advprog.pandacare.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RatingTest {

    @Test
    void testNoArgsConstructor() {
        Rating rating = new Rating();
        assertNotNull(rating);
    }

    @Test
    void testAllArgsConstructor() {
        Doctor doctor = new Doctor();
        Patient patient = new Patient();
        Consultation consultation = new Consultation();
        
        Rating rating = new Rating(1L, doctor, patient, consultation, 5, "Excellent service", null, null);
        
        assertEquals(1L, rating.getId());
        assertEquals(doctor, rating.getDoctor());
        assertEquals(patient, rating.getPatient());
        assertEquals(consultation, rating.getConsultation());
        assertEquals(5, rating.getScore());
        assertEquals("Excellent service", rating.getReview());
    }
    
    @Test
    void testSettersAndGetters() {
        Rating rating = new Rating();
        Doctor doctor = new Doctor();
        Patient patient = new Patient();
        Consultation consultation = new Consultation();
        
        rating.setId(1L);
        rating.setDoctor(doctor);
        rating.setPatient(patient);
        rating.setConsultation(consultation);
        rating.setScore(4);
        rating.setReview("Good experience");
        
        assertEquals(1L, rating.getId());
        assertEquals(doctor, rating.getDoctor());
        assertEquals(patient, rating.getPatient());
        assertEquals(consultation, rating.getConsultation());
        assertEquals(4, rating.getScore());
        assertEquals("Good experience", rating.getReview());
    }
    
    @Test
    void testInvalidScoreTooLow() {
        Rating rating = new Rating();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rating.setScore(0);
        });
        
        assertEquals("Rating score must be between 1 and 5", exception.getMessage());
    }
    
    @Test
    void testInvalidScoreTooHigh() {
        Rating rating = new Rating();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rating.setScore(6);
        });
        
        assertEquals("Rating score must be between 1 and 5", exception.getMessage());
    }
    
    @Test
    void testToString() {
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Smith");
        
        Patient patient = new Patient();
        patient.setName("John Doe");
        
        Rating rating = new Rating();
        rating.setId(1L);
        rating.setDoctor(doctor);
        rating.setPatient(patient);
        rating.setScore(5);
        rating.setReview("Excellent");
        
        String expected = "Rating{id=1, doctor=Dr. Smith, patient=John Doe, score=5, review='Excellent'}";
        assertEquals(expected, rating.toString());
    }
}