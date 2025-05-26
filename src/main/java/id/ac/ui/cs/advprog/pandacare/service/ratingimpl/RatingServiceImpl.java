package id.ac.ui.cs.advprog.pandacare.service.ratingimpl;

import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import id.ac.ui.cs.advprog.pandacare.model.Rating;
import id.ac.ui.cs.advprog.pandacare.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.pandacare.repository.RatingRepository;
import id.ac.ui.cs.advprog.pandacare.repository.PatientRepository;
import id.ac.ui.cs.advprog.pandacare.request.RatingRequest;
import id.ac.ui.cs.advprog.pandacare.service.RatingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ConsultationRepository consultationRepository;
    private final PatientRepository patientRepository; // Add this field

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, 
                           ConsultationRepository consultationRepository,
                           PatientRepository patientRepository) { // Add to constructor
        this.ratingRepository = ratingRepository;
        this.consultationRepository = consultationRepository;
        this.patientRepository = patientRepository; // Initialize it
    }

    @Override
    public Rating createRating(Long consultationId, int score, String review) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation not found"));

        Rating existingRating = ratingRepository.findByConsultation(consultation);
        if (existingRating != null) {
            throw new IllegalStateException("Rating already exists for this consultation");
        }
        
        Rating rating = new Rating();
        rating.setConsultation(consultation);
        rating.setDoctor(consultation.getDoctor());
        rating.setPatient(consultation.getPatient());
        rating.setScore(score);
        rating.setReview(review);

        return ratingRepository.save(rating);
    }

    @Override
    public Rating getRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found with id: " + id));
    }

    @Override
    public List<Rating> getRatingsByDoctor(Doctor doctor) {
        return ratingRepository.findByDoctor(doctor);
    }

    @Override
    public List<Rating> getRatingsByDoctorId(Long doctorId) {
        return ratingRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<Rating> getRatingsByPatient(Patient patient) {
        return ratingRepository.findByPatient(patient);
    }

    @Override
    public List<Rating> getRatingsByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
        return ratingRepository.findByPatient(patient);
    }

    @Override
    public Rating getRatingByConsultation(Consultation consultation) {
        return ratingRepository.findByConsultation(consultation);
    }

    @Override
    public Rating updateRating(Long id, int score, String review) {
        Rating rating = getRatingById(id);
        rating.setScore(score);
        rating.setReview(review);
        return ratingRepository.save(rating);
    }

    @Override
    public void deleteRating(Long id) {
        Rating rating = getRatingById(id);
        ratingRepository.delete(rating);
    }

    public double getAverageRating(Doctor doctor) {
        List<Rating> ratings = getRatingsByDoctor(doctor);
        if (ratings.isEmpty()) {
            return 0.0;
        }
        
        int sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getScore();
        }
        
        return (double) sum / ratings.size();
    }

    @Override
    public double getAverageRatingByDoctorId(Long doctorId) {
        List<Rating> ratings = getRatingsByDoctorId(doctorId);
        if (ratings.isEmpty()) {
            return 0.0;
        }
        
        int sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getScore();
        }
        
        return (double) sum / ratings.size();
    }

    public Rating updateRating(Long id, RatingRequest request) {
        Rating rating = ratingRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rating not found"));
        
        rating.setScore(request.getScore());
        rating.setReview(request.getReview());
        
        return ratingRepository.save(rating);
    }

    public Rating createRating(RatingRequest request) {
        Consultation consultation = consultationRepository.findById(request.getConsultationId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation not found"));
        
        Rating existingRating = ratingRepository.findByConsultation(consultation);
        if (existingRating != null) {
            // Update existing rating
            existingRating.setScore(request.getScore());
            existingRating.setReview(request.getReview());
            return ratingRepository.save(existingRating);
        }
        
        // Create new rating if none exists
        Rating newRating = new Rating();
        newRating.setConsultation(consultation);
        newRating.setDoctor(consultation.getDoctor());
        newRating.setPatient(consultation.getPatient());
        newRating.setScore(request.getScore());
        newRating.setReview(request.getReview());
        
        return ratingRepository.save(newRating);
    }

    @Override
    public Rating getRatingByConsultationId(Long consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Consultation not found with id: " + consultationId));
        
        Rating rating = ratingRepository.findByConsultation(consultation);
        if (rating == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Rating not found for consultation with id: " + consultationId);
        }
        
        return rating;
    }
}