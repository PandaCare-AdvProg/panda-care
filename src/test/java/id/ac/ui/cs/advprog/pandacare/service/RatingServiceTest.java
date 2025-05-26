package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import id.ac.ui.cs.advprog.pandacare.model.Rating;
import id.ac.ui.cs.advprog.pandacare.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.pandacare.repository.RatingRepository;
import id.ac.ui.cs.advprog.pandacare.repository.PatientRepository;
import id.ac.ui.cs.advprog.pandacare.service.ratingimpl.RatingServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;
    
    @Mock
    private ConsultationRepository consultationRepository;
    
    @Mock
    private PatientRepository patientRepository;
    
    private RatingService ratingService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ratingService = new RatingServiceImpl(ratingRepository, consultationRepository, patientRepository);
    }
    
    @Test
    void createRating_shouldSaveRating() {
        // Setup
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");
        
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        
        Consultation consultation = new Consultation();
        consultation.setId(1L);
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        
        when(consultationRepository.findById(1L)).thenReturn(Optional.of(consultation));
        when(ratingRepository.findByConsultation(consultation)).thenReturn(null);
        
        Rating savedRating = new Rating();
        savedRating.setId(1L);
        savedRating.setDoctor(doctor);
        savedRating.setPatient(patient);
        savedRating.setConsultation(consultation);
        savedRating.setScore(5);
        savedRating.setReview("Excellent");
        
        when(ratingRepository.save(any(Rating.class))).thenReturn(savedRating);
        
        // Execute
        Rating result = ratingService.createRating(1L, 5, "Excellent");
        
        // Verify
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(5, result.getScore());
        assertEquals("Excellent", result.getReview());
        assertEquals(doctor, result.getDoctor());
        assertEquals(patient, result.getPatient());
        
        verify(consultationRepository).findById(1L);
        verify(ratingRepository).findByConsultation(consultation);
        verify(ratingRepository).save(any(Rating.class));
    }
      @Test
    void createRating_shouldThrowExceptionIfConsultationNotFound() {
        // Setup
        when(consultationRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Execute & Verify
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            ratingService.createRating(1L, 5, "Excellent");
        });
        
        // Check the exact exception message from your implementation
        assertEquals("Consultation not found", exception.getReason());
        verify(consultationRepository).findById(1L);
        verify(ratingRepository, never()).save(any(Rating.class));
    }
      @Test
    void createRating_shouldThrowExceptionIfRatingExists() {
        // Setup
        Doctor doctor = new Doctor();
        Patient patient = new Patient();
        
        Consultation consultation = new Consultation();
        consultation.setId(1L);
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        
        Rating existingRating = new Rating();
        existingRating.setId(1L);
        
        when(consultationRepository.findById(1L)).thenReturn(Optional.of(consultation));
        when(ratingRepository.findByConsultation(consultation)).thenReturn(existingRating);
        
        // Execute & Verify
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            ratingService.createRating(1L, 5, "Excellent");
        });
        
        assertEquals("Rating already exists for this consultation", exception.getMessage());
        verify(consultationRepository).findById(1L);
        verify(ratingRepository).findByConsultation(consultation);
        verify(ratingRepository, never()).save(any(Rating.class));
    }
    
    @Test
    void getRatingById_shouldReturnRating() {
        // Setup
        Rating rating = new Rating();
        rating.setId(1L);
        rating.setScore(5);
        
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating));
        
        // Execute
        Rating result = ratingService.getRatingById(1L);
        
        // Verify
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(5, result.getScore());
        verify(ratingRepository).findById(1L);
    }
    
    @Test
    void getRatingById_shouldThrowExceptionWhenNotFound() {
        // Setup
        when(ratingRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Execute & Verify
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ratingService.getRatingById(1L);
        });
        
        assertEquals("Rating not found with id: 1", exception.getMessage());
        verify(ratingRepository).findById(1L);
    }
    
    @Test
    void getRatingsByDoctor_shouldReturnDoctorRatings() {
        // Setup
        Doctor doctor = new Doctor();
        
        Rating rating1 = new Rating();
        rating1.setId(1L);
        rating1.setScore(5);
        
        Rating rating2 = new Rating();
        rating2.setId(2L);
        rating2.setScore(4);
        
        List<Rating> ratings = Arrays.asList(rating1, rating2);
        
        when(ratingRepository.findByDoctor(doctor)).thenReturn(ratings);
        
        // Execute
        List<Rating> result = ratingService.getRatingsByDoctor(doctor);
        
        // Verify
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(5, result.get(0).getScore());
        assertEquals(4, result.get(1).getScore());
        verify(ratingRepository).findByDoctor(doctor);
    }
    
    @Test
    void updateRating_shouldUpdateExistingRating() {
        // Setup
        Rating existingRating = new Rating();
        existingRating.setId(1L);
        existingRating.setScore(3);
        existingRating.setReview("Average");
        
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(i -> i.getArgument(0));
        
        // Execute
        Rating result = ratingService.updateRating(1L, 5, "Excellent");
        
        // Verify
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(5, result.getScore());
        assertEquals("Excellent", result.getReview());
        verify(ratingRepository).findById(1L);
        verify(ratingRepository).save(existingRating);
    }
    
    @Test
    void deleteRating_shouldDeleteRating() {
        // Setup
        Rating rating = new Rating();
        rating.setId(1L);
        
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating));
        doNothing().when(ratingRepository).delete(rating);
        
        // Execute
        ratingService.deleteRating(1L);
        
        // Verify
        verify(ratingRepository).findById(1L);
        verify(ratingRepository).delete(rating);
    }
    
    @Test
    void getAverageRating_shouldCalculateAverage() {
        // Setup
        Doctor doctor = new Doctor();
        
        Rating rating1 = new Rating();
        rating1.setScore(5);
        
        Rating rating2 = new Rating();
        rating2.setScore(3);
        
        List<Rating> ratings = Arrays.asList(rating1, rating2);
        
        when(ratingRepository.findByDoctor(doctor)).thenReturn(ratings);
        
        // Execute
        double result = ratingService.getAverageRating(doctor);
        
        // Verify
        assertEquals(4.0, result);
        verify(ratingRepository).findByDoctor(doctor);
    }
    
    @Test
    void getAverageRating_shouldReturnZeroWhenNoRatings() {
        // Setup
        Doctor doctor = new Doctor();
        
        when(ratingRepository.findByDoctor(doctor)).thenReturn(Arrays.asList());
        
        // Execute
        double result = ratingService.getAverageRating(doctor);
        
        // Verify
        assertEquals(0.0, result);
        verify(ratingRepository).findByDoctor(doctor);
    }
    
    @Test
    void getAverageRatingByDoctorId_shouldCalculateAverage() {
        // Setup
        Rating rating1 = new Rating();
        rating1.setScore(5);
        
        Rating rating2 = new Rating();
        rating2.setScore(3);
        
        List<Rating> ratings = Arrays.asList(rating1, rating2);
        
        when(ratingRepository.findByDoctorId(1L)).thenReturn(ratings);
        
        // Execute
        double result = ratingService.getAverageRatingByDoctorId(1L);
        
        // Verify
        assertEquals(4.0, result);
        verify(ratingRepository).findByDoctorId(1L);
    }
}