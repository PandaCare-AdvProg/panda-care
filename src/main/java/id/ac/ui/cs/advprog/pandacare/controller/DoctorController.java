package id.ac.ui.cs.advprog.pandacare.controller;

import java.util.List;

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
            return service.searchByName(name);
        }
        if (specialty != null) {
            return service.searchBySpecialty(specialty);
        }
        return service.getAllDoctors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getById(@PathVariable Long id) {
        return service.getDoctorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Doctor create(@RequestBody Doctor doctor) {
        return service.createDoctor(doctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> update(
            @PathVariable Long id,
            @RequestBody Doctor doctor
    ) {
        try {
            Doctor updated = service.updateDoctor(id, doctor);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/me")
    public ResponseEntity<Doctor> getCurrentDoctor() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorRepository.findDoctorByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}