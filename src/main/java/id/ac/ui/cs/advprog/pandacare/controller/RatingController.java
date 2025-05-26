package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.model.Rating;
import id.ac.ui.cs.advprog.pandacare.request.RatingRequest;
import id.ac.ui.cs.advprog.pandacare.response.RatingResponse;
import id.ac.ui.cs.advprog.pandacare.service.RatingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<RatingResponse> createRating(@RequestBody RatingRequest request) {
        // Check if a rating already exists for this consultation
        try {
            // First find if there's an existing rating for this consultation
            Rating existingRating = ratingService.getRatingByConsultationId(request.getConsultationId());
            
            // If found, update it instead of creating a new one
            Rating updated = ratingService.updateRating(
                    existingRating.getId(),
                    request.getScore(),
                    request.getReview()
            );
            
            return ResponseEntity.ok(mapToResponse(updated));
        } catch (Exception e) {
            // If no existing rating found or other error, create a new one
            Rating rating = ratingService.createRating(request);
            return ResponseEntity.ok(mapToResponse(rating));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingResponse> getRatingById(@PathVariable Long id) {
        Rating rating = ratingService.getRatingById(id);
        return ResponseEntity.ok(mapToResponse(rating));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<RatingResponse>> getRatingsByDoctorId(@PathVariable Long doctorId) {
        List<Rating> ratings = ratingService.getRatingsByDoctorId(doctorId);
        List<RatingResponse> responses = ratings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<RatingResponse> updateRating(@PathVariable Long id, @RequestBody RatingRequest request) {
        Rating rating = ratingService.updateRating(
                id,
                request.getScore(),
                request.getReview()
        );
        
        return ResponseEntity.ok(mapToResponse(rating));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<String> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.ok("Rating deleted successfully");
    }

    @GetMapping("/doctor/{doctorId}/average")
    public ResponseEntity<Double> getDoctorAverageRating(@PathVariable Long doctorId) {
        double averageRating = ratingService.getAverageRatingByDoctorId(doctorId);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<RatingResponse>> getRatingsByPatientId(@PathVariable Long patientId) {
        List<Rating> ratings = ratingService.getRatingsByPatientId(patientId);
        List<RatingResponse> responses = ratings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/consultation/{consultationId}")
    public ResponseEntity<RatingResponse> getRatingByConsultationId(@PathVariable Long consultationId) {
        try {
            Rating rating = ratingService.getRatingByConsultationId(consultationId);
            return ResponseEntity.ok(mapToResponse(rating));
        } catch (ResponseStatusException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.notFound().build();
            }
            throw e;
        }
    }
    
    // Helper method to map Rating entity to RatingResponse DTO
    private RatingResponse mapToResponse(Rating rating) {
        return RatingResponse.builder()
                .id(rating.getId())
                .doctorId(rating.getDoctor().getId())
                .doctorName(rating.getDoctor().getName())
                .patientId(rating.getPatient().getId())
                .patientName(rating.getPatient().getName())
                .consultationId(rating.getConsultation().getId())
                .score(rating.getScore())
                .review(rating.getReview())
                .build();
    }
}
