package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import id.ac.ui.cs.advprog.pandacare.model.Rating;
import id.ac.ui.cs.advprog.pandacare.request.RatingRequest;
import id.ac.ui.cs.advprog.pandacare.response.RatingResponse;
import id.ac.ui.cs.advprog.pandacare.service.RatingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RatingControllerTest {

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRating_shouldReturnCreatedRating() {
        // Setup
        RatingRequest request = new RatingRequest();
        request.setConsultationId(1L);
        request.setScore(5);
        request.setReview("Excellent service");

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");

        Patient patient = new Patient();
        patient.setId(2L);
        patient.setName("John Doe");

        Consultation consultation = new Consultation();
        consultation.setId(1L);
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);

        Rating rating = new Rating();
        rating.setId(1L);
        rating.setDoctor(doctor);
        rating.setPatient(patient);
        rating.setConsultation(consultation);
        rating.setScore(5);
        rating.setReview("Excellent service");

        when(ratingService.createRating(eq(1L), eq(5), eq("Excellent service"))).thenReturn(rating);

        // Execute
        ResponseEntity<RatingResponse> response = ratingController.createRating(request);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(5, response.getBody().getScore());
        assertEquals("Excellent service", response.getBody().getReview());
        verify(ratingService).createRating(1L, 5, "Excellent service");
    }

    @Test
    void getRatingById_shouldReturnRating() {
        // Setup
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");

        Patient patient = new Patient();
        patient.setId(2L);
        patient.setName("John Doe");

        Consultation consultation = new Consultation();
        consultation.setId(3L);
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);

        Rating rating = new Rating();
        rating.setId(1L);
        rating.setDoctor(doctor);
        rating.setPatient(patient);
        rating.setConsultation(consultation);
        rating.setScore(4);
        rating.setReview("Good service");

        when(ratingService.getRatingById(1L)).thenReturn(rating);

        // Execute
        ResponseEntity<RatingResponse> response = ratingController.getRatingById(1L);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(4, response.getBody().getScore());
        assertEquals("Good service", response.getBody().getReview());
        verify(ratingService).getRatingById(1L);
    }

    @Test
    void getRatingsByDoctorId_shouldReturnDoctorRatings() {
        // Setup
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");

        Patient patient1 = new Patient();
        patient1.setId(2L);
        patient1.setName("John Doe");

        Patient patient2 = new Patient();
        patient2.setId(3L);
        patient2.setName("Jane Smith");

        Consultation consultation1 = new Consultation();
        consultation1.setId(4L);
        consultation1.setDoctor(doctor);
        consultation1.setPatient(patient1);

        Consultation consultation2 = new Consultation();
        consultation2.setId(5L);
        consultation2.setDoctor(doctor);
        consultation2.setPatient(patient2);

        Rating rating1 = new Rating();
        rating1.setId(1L);
        rating1.setDoctor(doctor);
        rating1.setPatient(patient1);
        rating1.setConsultation(consultation1);
        rating1.setScore(5);
        rating1.setReview("Excellent");

        Rating rating2 = new Rating();
        rating2.setId(2L);
        rating2.setDoctor(doctor);
        rating2.setPatient(patient2);
        rating2.setConsultation(consultation2);
        rating2.setScore(4);
        rating2.setReview("Very good");

        List<Rating> ratings = Arrays.asList(rating1, rating2);
        when(ratingService.getRatingsByDoctorId(1L)).thenReturn(ratings);

        // Execute
        ResponseEntity<List<RatingResponse>> response = ratingController.getRatingsByDoctorId(1L);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(5, response.getBody().get(0).getScore());
        assertEquals(4, response.getBody().get(1).getScore());
        verify(ratingService).getRatingsByDoctorId(1L);
    }

    @Test
    void updateRating_shouldReturnUpdatedRating() {
        // Setup
        RatingRequest request = new RatingRequest();
        request.setScore(5);
        request.setReview("Updated review");

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");

        Patient patient = new Patient();
        patient.setId(2L);
        patient.setName("John Doe");

        Consultation consultation = new Consultation();
        consultation.setId(3L);
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);

        Rating rating = new Rating();
        rating.setId(1L);
        rating.setDoctor(doctor);
        rating.setPatient(patient);
        rating.setConsultation(consultation);
        rating.setScore(5);
        rating.setReview("Updated review");

        when(ratingService.updateRating(eq(1L), eq(5), eq("Updated review"))).thenReturn(rating);

        // Execute
        ResponseEntity<RatingResponse> response = ratingController.updateRating(1L, request);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(5, response.getBody().getScore());
        assertEquals("Updated review", response.getBody().getReview());
        verify(ratingService).updateRating(1L, 5, "Updated review");
    }

    @Test
    void deleteRating_shouldReturnSuccessMessage() {
        // Setup
        doNothing().when(ratingService).deleteRating(1L);

        // Execute
        ResponseEntity<String> response = ratingController.deleteRating(1L);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Rating deleted successfully", response.getBody());
        verify(ratingService).deleteRating(1L);
    }

    @Test
    void getDoctorAverageRating_shouldReturnAverageRating() {
        // Setup
        when(ratingService.getAverageRatingByDoctorId(1L)).thenReturn(4.5);

        // Execute
        ResponseEntity<Double> response = ratingController.getDoctorAverageRating(1L);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4.5, response.getBody());
        verify(ratingService).getAverageRatingByDoctorId(1L);
    }
}