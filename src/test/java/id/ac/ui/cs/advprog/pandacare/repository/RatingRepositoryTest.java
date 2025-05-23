package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import id.ac.ui.cs.advprog.pandacare.model.Rating;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RatingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    void findByDoctor_shouldReturnDoctorRatings() {
        // Create test data
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Smith");
        doctor.setEmail("drsmith@example.com");
        doctor.setRole(Role.DOCTOR); 
        entityManager.persist(doctor);
        
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setRole(Role.PATIENT); 
        entityManager.persist(patient);
        
        Consultation consultation = new Consultation();
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        consultation.setScheduledTime(LocalTime.now()); 
        consultation.setDayOfWeek(DayOfWeek.MONDAY);
        entityManager.persist(consultation);
        
        Rating rating = new Rating();
        rating.setDoctor(doctor);
        rating.setPatient(patient);
        rating.setConsultation(consultation);
        rating.setScore(5);
        rating.setReview("Great doctor");
        entityManager.persist(rating);
        
        // Test findByDoctor method
        List<Rating> foundRatings = ratingRepository.findByDoctor(doctor);
        
        assertThat(foundRatings).hasSize(1);
        assertThat(foundRatings.get(0).getScore()).isEqualTo(5);
        assertThat(foundRatings.get(0).getReview()).isEqualTo("Great doctor");
    }
    
    @Test
    void findByPatient_shouldReturnPatientRatings() {
        // Create test data
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Smith");
        doctor.setEmail("drsmith@example.com");
        doctor.setRole(Role.DOCTOR); 
        entityManager.persist(doctor);
        
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setRole(Role.PATIENT); 
        entityManager.persist(patient);
        
        Consultation consultation = new Consultation();
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        consultation.setScheduledTime(LocalTime.now()); 
        consultation.setDayOfWeek(DayOfWeek.MONDAY);
        entityManager.persist(consultation);
        
        Rating rating = new Rating();
        rating.setDoctor(doctor);
        rating.setPatient(patient);
        rating.setConsultation(consultation);
        rating.setScore(4);
        rating.setReview("Good experience");
        entityManager.persist(rating);
        
        // Test findByPatient method
        List<Rating> foundRatings = ratingRepository.findByPatient(patient);
        
        assertThat(foundRatings).hasSize(1);
        assertThat(foundRatings.get(0).getScore()).isEqualTo(4);
        assertThat(foundRatings.get(0).getReview()).isEqualTo("Good experience");
    }
    
    @Test
    void findByConsultation_shouldReturnConsultationRating() {
        // Create test data
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Smith");
        doctor.setEmail("drsmith@example.com");
        doctor.setRole(Role.DOCTOR); 
        entityManager.persist(doctor);
        
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setRole(Role.PATIENT); 
        entityManager.persist(patient);
        
        Consultation consultation = new Consultation();
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        consultation.setScheduledTime(LocalTime.now()); 
        consultation.setDayOfWeek(DayOfWeek.MONDAY);
        entityManager.persist(consultation);
        
        Rating rating = new Rating();
        rating.setDoctor(doctor);
        rating.setPatient(patient);
        rating.setConsultation(consultation);
        rating.setScore(3);
        rating.setReview("Average");
        entityManager.persist(rating);
        
        // Test findByConsultation method
        Rating foundRating = ratingRepository.findByConsultation(consultation);
        
        assertThat(foundRating).isNotNull();
        assertThat(foundRating.getScore()).isEqualTo(3);
        assertThat(foundRating.getReview()).isEqualTo("Average");
    }
    
    @Test
    void findByDoctorId_shouldReturnDoctorRatings() {
        // Create test data
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Smith");
        doctor.setEmail("drsmith@example.com");
        doctor.setRole(Role.DOCTOR); 
        entityManager.persist(doctor);
        
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setRole(Role.PATIENT); 
        entityManager.persist(patient);
        
        Consultation consultation = new Consultation();
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        consultation.setScheduledTime(LocalTime.now()); 
        consultation.setDayOfWeek(DayOfWeek.MONDAY);
        entityManager.persist(consultation);
        
        Rating rating = new Rating();
        rating.setDoctor(doctor);
        rating.setPatient(patient);
        rating.setConsultation(consultation);
        rating.setScore(5);
        rating.setReview("Excellent");
        entityManager.persist(rating);
        
        // Test findByDoctorId method
        List<Rating> foundRatings = ratingRepository.findByDoctorId(doctor.getId());
        
        assertThat(foundRatings).hasSize(1);
        assertThat(foundRatings.get(0).getScore()).isEqualTo(5);
        assertThat(foundRatings.get(0).getReview()).isEqualTo("Excellent");
    }
}