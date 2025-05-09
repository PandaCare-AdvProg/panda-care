package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Alice");
        doctor.setSpecialty("Cardiology");
    }

    @Test
    void testCreateDoctor() {
        when(doctorService.createDoctor(any(Doctor.class))).thenReturn(doctor);

        Doctor result = doctorController.create(doctor);

        assertEquals(doctor, result);
        verify(doctorService).createDoctor(doctor);
    }

    @Test
    void testUpdateDoctor() {
        when(doctorService.updateDoctor(eq(1L), any(Doctor.class))).thenReturn(doctor);

        ResponseEntity<Doctor> response = doctorController.update(1L, doctor);

        assertEquals(doctor, response.getBody());
        verify(doctorService).updateDoctor(1L, doctor);
    }

    @Test
    void testUpdateDoctorNotFound() {
        when(doctorService.updateDoctor(eq(1L), any(Doctor.class))).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<Doctor> response = doctorController.update(1L, doctor);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetDoctorByIdFound() {
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(doctor));

        ResponseEntity<Doctor> response = doctorController.getById(1L);

        assertEquals(doctor, response.getBody());
    }

    @Test
    void testGetDoctorByIdNotFound() {
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Doctor> response = doctorController.getById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testListAllDoctors() {
        when(doctorService.getAllDoctors()).thenReturn(List.of(doctor));

        List<Doctor> result = doctorController.listAll(null, null);

        assertEquals(1, result.size());
        assertEquals(doctor, result.get(0));
    }

    @Test
    void testSearchByName() {
        when(doctorService.searchByName("Alice")).thenReturn(List.of(doctor));

        List<Doctor> result = doctorController.listAll("Alice", null);

        assertEquals(1, result.size());
        assertEquals(doctor, result.get(0));
    }

    @Test
    void testSearchBySpecialty() {
        when(doctorService.searchBySpecialty("Cardiology")).thenReturn(List.of(doctor));

        List<Doctor> result = doctorController.listAll(null, "Cardiology");

        assertEquals(1, result.size());
        assertEquals(doctor, result.get(0));
    }

    @Test
    void testDeleteDoctor() {
        ResponseEntity<Void> response = doctorController.delete(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(doctorService).deleteDoctor(1L);
    }
}
