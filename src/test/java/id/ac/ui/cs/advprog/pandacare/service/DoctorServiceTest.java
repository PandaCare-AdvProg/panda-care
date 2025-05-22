package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DoctorServiceTest {

    private DoctorRepository repo;
    private DoctorService service;

    @BeforeEach
    void setUp() {
        repo = mock(DoctorRepository.class);
        service = new DoctorServiceImpl(repo); // use the implementation
    }

    @Test
    void testGetAllDoctors() {
        List<Doctor> doctors = List.of(new Doctor(), new Doctor());
        when(repo.findAll()).thenReturn(doctors);

        List<Doctor> result = service.getAllDoctors();
        assertEquals(2, result.size());
    }

    @Test
    void testGetDoctorByIdFound() {
        Doctor doctor = new Doctor();
        doctor.setName("Alice");
        when(repo.findById(1L)).thenReturn(Optional.of(doctor));

        Optional<Doctor> result = service.getDoctorById(1L);
        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getName());
    }

    @Test
    void testGetDoctorByIdNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        Optional<Doctor> result = service.getDoctorById(99L);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateDoctor() {
        Doctor doctor = new Doctor();
        doctor.setName("Bob");
        when(repo.save(doctor)).thenReturn(doctor);

        Doctor result = service.createDoctor(doctor);
        assertEquals("Bob", result.getName());
    }

    @Test
    void testUpdateDoctor() {
        Doctor old = new Doctor();
        old.setName("Old");

        Doctor newDoc = new Doctor();
        newDoc.setName("New");

        when(repo.findById(1L)).thenReturn(Optional.of(old));
        when(repo.save(any(Doctor.class))).thenReturn(newDoc);

        Doctor result = service.updateDoctor(1L, newDoc);
        assertEquals("New", result.getName());
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
        service.searchBySpecialty("derma");
        verify(repo).findBySpecialtyContainingIgnoreCase("derma");
    }
}
