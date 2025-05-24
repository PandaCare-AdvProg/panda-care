package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import id.ac.ui.cs.advprog.pandacare.repository.PatientRepository;
import id.ac.ui.cs.advprog.pandacare.service.ConsultationService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;
    private final PatientRepository patientRepository;


    @PostMapping
    public ResponseEntity<Consultation> createConsultation(@RequestBody Consultation consultation) {
        Consultation saved = consultationService.createConsultation(consultation);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consultation> updateConsultation(
            @PathVariable("id") Long consultationId,
            @RequestBody Consultation updatedConsultation
    ) {
        Consultation updated = consultationService.updateConsultation(consultationId, updatedConsultation);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Consultation> approveConsultation(@PathVariable("id") Long consultationId) {
        Consultation approved = consultationService.approveConsultation(consultationId);
        return ResponseEntity.ok(approved);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Consultation> rejectConsultation(@PathVariable("id") Long consultationId) {
        Consultation rejected = consultationService.rejectConsultation(consultationId);
        return ResponseEntity.ok(rejected);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Consultation> completeConsultation(@PathVariable("id") Long consultationId) {
        Consultation completed = consultationService.completeConsultation(consultationId);
        return ResponseEntity.ok(completed);
    }
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Consultation>> getConsultationsByDoctor(@PathVariable("doctorId") Long doctorId) {   
    List<Consultation> consultations = consultationService.getConsultationsByDoctor(doctorId);
    return ResponseEntity.ok(consultations);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Consultation>> getConsultationsByPatient(@PathVariable("patientId") Long patientId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patient patient = patientRepository.findPatientByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with email: " + email));
        
        if (!patient.getId().equals(patientId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<Consultation> consultations = consultationService.getConsultationsByPatient(patientId);
        return ResponseEntity.ok(consultations);
    }
}