package id.ac.ui.cs.advprog.pandacare.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    @Test
    void testNoArgsConstructor() {
        Patient patient = new Patient();
        assertNotNull(patient);
    }

    @Test
    void testAllArgsConstructor() {
        Patient patient = new Patient(
                "test@example.com",
                "password123",
                "John Doe",
                "1234567890123456",
                "123 Main St",
                "081234567890",
                "No medical history"
        );

        assertEquals("test@example.com", patient.getEmail());
        assertEquals("password123", patient.getPassword());
        assertEquals("John Doe", patient.getName());
        assertEquals("1234567890123456", patient.getNik());
        assertEquals("123 Main St", patient.getAddress());
        assertEquals("081234567890", patient.getPhonenum());
        assertEquals("No medical history", patient.getMedicalHistory());
    }

    @Test
    void testSettersAndGetters() {
        Patient patient = new Patient();
        patient.setEmail("test@example.com");
        patient.setPassword("password123");
        patient.setName("John Doe");
        patient.setNik("1234567890123456");
        patient.setAddress("123 Main St");
        patient.setPhonenum("081234567890");
        patient.setMedicalHistory("No medical history");

        assertEquals("test@example.com", patient.getEmail());
        assertEquals("password123", patient.getPassword());
        assertEquals("John Doe", patient.getName());
        assertEquals("1234567890123456", patient.getNik());
        assertEquals("123 Main St", patient.getAddress());
        assertEquals("081234567890", patient.getPhonenum());
        assertEquals("No medical history", patient.getMedicalHistory());
    }

    @Test
    void testToString() {
        Patient patient = new Patient(
                "test@example.com",
                "password123",
                "John Doe",
                "1234567890123456",
                "123 Main St",
                "081234567890",
                "No medical history"
        );

        String expected = "Patient{id=null, email='test@example.com', name='John Doe', nik='1234567890123456', address='123 Main St', phoneNumber='081234567890', medicalHistory='No medical history'}";
        assertEquals(expected, patient.toString());
    }
}