package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import id.ac.ui.cs.advprog.pandacare.model.Rating;
import id.ac.ui.cs.advprog.pandacare.request.RatingRequest;

import java.util.List;

public interface RatingService {
    // Create a new rating
    Rating createRating(Long consultationId, int score, String review);
    Rating createRating(RatingRequest request);
    
    // Get a rating by ID
    Rating getRatingById(Long id);
    
    // Get all ratings for a doctor
    List<Rating> getRatingsByDoctor(Doctor doctor);
    
    // Get all ratings for a doctor by ID
    List<Rating> getRatingsByDoctorId(Long doctorId);
    
    // Get all ratings by a patient
    List<Rating> getRatingsByPatient(Patient patient);
    
    // Get rating for a specific consultation
    Rating getRatingByConsultation(Consultation consultation);
    Rating getRatingByConsultationId(Long consultationId);
    
    // Update an existing rating
    Rating updateRating(Long id, int score, String review);
    Rating updateRating(Long id, RatingRequest request);
    
    // Delete a rating
    void deleteRating(Long id);
    
    // Calculate average rating for a doctor
    double getAverageRating(Doctor doctor);
    
    // Calculate average rating for a doctor by ID
    double getAverageRatingByDoctorId(Long doctorId);

    // Get all ratings by a patient ID
    List<Rating> getRatingsByPatientId(Long patientId);
}