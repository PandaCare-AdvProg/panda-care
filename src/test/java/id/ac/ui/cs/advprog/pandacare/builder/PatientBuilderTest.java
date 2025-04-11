package id.ac.ui.cs.advprog.pandacare.builder;

import id.ac.ui.cs.advprog.pandacare.model.Patient;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class PatientBuilderTest {

    @Test
    void testSetters() {
        PatientBuilder builder = new PatientBuilder();
        builder.setEmail("test@example.com")
                .setPassword("password123")
                .setName("John Doe")
                .setNik("1234567890")
                .setAddress("123 Main St")
                .setPhoneNumber("081234567890")
                .setMedicalHistory("No known allergies");

        assertEquals("test@example.com", builder.build().getEmail());
        assertEquals("password123", builder.build().getPassword());
        assertEquals("John Doe", builder.build().getName());
        assertEquals("1234567890", builder.build().getNik());
        assertEquals("123 Main St", builder.build().getAddress());
        assertEquals("081234567890", builder.build().getPhonenum());
        assertEquals("No known allergies", builder.build().getMedicalHistory());
    }

    @Test
    void testBuildCreatesPatient() {
        PatientBuilder builder = new PatientBuilder();
        Patient patient = builder.setEmail("test@example.com")
                .setPassword("password123")
                .setName("John Doe")
                .setNik("1234567890")
                .setAddress("123 Main St")
                .setPhoneNumber("081234567890")
                .setMedicalHistory("No known allergies")
                .build();

        assertNotNull(patient);
        assertEquals("test@example.com", patient.getEmail());
        assertEquals("password123", patient.getPassword());
        assertEquals("John Doe", patient.getName());
        assertEquals("1234567890", patient.getNik());
        assertEquals("123 Main St", patient.getAddress());
        assertEquals("081234567890", patient.getPhonenum());
        assertEquals("No known allergies", patient.getMedicalHistory());
    }

    @Test
    void testBuildThrowsExceptionWhenRequiredFieldsAreMissing() {
        PatientBuilder builder = new PatientBuilder();
        builder.setEmail("test@example.com")
                .setPassword("password123")
                .setName("John Doe")
                .setNik("1234567890")
                .setAddress("123 Main St");

        Exception exception = assertThrows(IllegalStateException.class, builder::build);
        assertEquals("Required fields are missing", exception.getMessage());
    }
}