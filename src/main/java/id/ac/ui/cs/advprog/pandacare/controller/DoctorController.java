package id.ac.ui.cs.advprog.pandacare.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.repository.DoctorRepository;
import id.ac.ui.cs.advprog.pandacare.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private static final Logger log = LoggerFactory.getLogger(DoctorController.class);

    private final DoctorService service;
    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorController(DoctorService service, DoctorRepository doctorRepository) {
        this.service = service;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping
    public List<Doctor> listAll(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "specialty", required = false) String specialty
    ) {
        if (name != null) {
            log.info("Searching doctors by name: {}", name);
            return service.searchByName(name);
        }
        if (specialty != null) {
            log.info("Searching doctors by specialty: {}", specialty);
            return service.searchBySpecialty(specialty);
        }
        log.info("Listing all doctors");
        return service.getAllDoctors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getById(@PathVariable Long id) {
        log.info("Getting doctor by id: {}", id);
        return service.getDoctorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Doctor create(@RequestBody Doctor doctor) {
        log.info("Creating doctor: {}", doctor);
        return service.createDoctor(doctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> update(
            @PathVariable Long id,
            @RequestBody Doctor doctor
    ) {
        log.info("Updating doctor id {} with data: {}", id, doctor);
        try {
            Doctor updated = service.updateDoctor(id, doctor);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.warn("Doctor not found for update, id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting doctor with id: {}", id);
        service.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/me")
    public ResponseEntity<Doctor> getCurrentDoctor() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Getting current doctor for email: {}", email);
        return doctorRepository.findDoctorByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}