package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoctorServiceImplTest {

    private DoctorRepository repo;
    private DoctorServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = mock(DoctorRepository.class);
        service = new DoctorServiceImpl(repo);
    }

    @Test
    void testGetAllDoctors() {
        List<Doctor> doctors = List.of(new Doctor(), new Doctor());
        when(repo.findAll()).thenReturn(doctors);

        assertEquals(2, service.getAllDoctors().size());
    }

    @Test
    void testGetDoctorById() {
        Doctor doctor = new Doctor();
        when(repo.findById(1L)).thenReturn(Optional.of(doctor));

        Optional<Doctor> result = service.getDoctorById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void testCreateDoctor() {
        Doctor doctor = new Doctor();
        when(repo.save(doctor)).thenReturn(doctor);

        assertEquals(doctor, service.createDoctor(doctor));
    }

    @Test
    void testUpdateDoctorSuccess() {
        Doctor oldDoctor = new Doctor();
        oldDoctor.setName("Old");

        Doctor updated = new Doctor();
        updated.setName("New");

        when(repo.findById(1L)).thenReturn(Optional.of(oldDoctor));
        when(repo.save(any(Doctor.class))).thenReturn(updated);

        Doctor result = service.updateDoctor(1L, updated);
        assertEquals("New", result.getName());
    }

    @Test
    void testUpdateDoctorNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.updateDoctor(99L, new Doctor()));
    }

    @Test
    void testDeleteDoctor() {
        service.deleteDoctor(1L);
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void testSearchByName() {
        service.searchByName("john");
        verify(repo).findByNameContainingIgnoreCase("john");
    }

    @Test
    void testSearchBySpecialty() {
        service.searchBySpecialty("cardio");
        verify(repo).findBySpecialtyContainingIgnoreCase("cardio");
    }
}
