package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    @DisplayName("Should find patient by email")
    void testFindPatientByEmail() {
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setEmail("johndoe@example.com");
        patient.setRole(Role.PATIENT);
        patientRepository.save(patient);

        Optional<Patient> found = patientRepository.findPatientByEmail("johndoe@example.com");

        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
        assertEquals("johndoe@example.com", found.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty when email not found")
    void testFindPatientByEmailNotFound() {
        Optional<Patient> found = patientRepository.findPatientByEmail("notfound@example.com");
        assertFalse(found.isPresent());
    }
}