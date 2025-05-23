package id.ac.ui.cs.advprog.pandacare.builder;

import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.enums.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class DoctorBuilderTest {

    @Test
    void testSetters() {
        DoctorBuilder builder = new DoctorBuilder();
        builder.setEmail("test@example.com")
                .setPassword("password123")
                .setName("John Doe")
                .setNik("1234567890")
                .setAddress("123 Blue St")
                .setWorkingAddress("123 Main St")
                .setPhoneNumber("081234567890")
                .setRole(Role.DOCTOR)
                .setSpecialty("Radiology");



        assertEquals("test@example.com", builder.build().getEmail());
        assertEquals("password123", builder.build().getPassword());
        assertEquals("John Doe", builder.build().getName());
        assertEquals("1234567890", builder.build().getNik());
        assertEquals("123 Blue St", builder.build().getAddress());
        assertEquals("123 Main St", builder.build().getWorkingAddress());
        assertEquals("081234567890", builder.build().getPhonenum());
        assertEquals(Role.DOCTOR, builder.build().getRole());
        assertEquals("Radiology", builder.build().getSpecialty());
    }

    @Test
    void testBuildCreatesDoctor() {
        DoctorBuilder builder = new DoctorBuilder();
        Doctor Doctor = builder.setEmail("test@example.com")
                .setPassword("password123")
                .setName("John Doe")
                .setNik("1234567890")
                .setAddress("123 Blue St")
                .setWorkingAddress("123 Main St")
                .setPhoneNumber("081234567890")
                .setRole(Role.DOCTOR)
                .setSpecialty("Dermatology")
                .build();

        assertNotNull(Doctor);
        assertEquals("test@example.com", Doctor.getEmail());
        assertEquals("password123", Doctor.getPassword());
        assertEquals("John Doe", Doctor.getName());
        assertEquals("1234567890", Doctor.getNik());
        assertEquals("123 Blue St", Doctor.getAddress());
        assertEquals("123 Main St", Doctor.getWorkingAddress());
        assertEquals("081234567890", Doctor.getPhonenum());
        assertEquals(Role.DOCTOR, builder.build().getRole());
        assertEquals("Dermatology", Doctor.getSpecialty());
    }

    @Test
    void testBuildThrowsExceptionWhenRequiredFieldsAreMissing() {
        DoctorBuilder builder = new DoctorBuilder();
        builder.setEmail("test@example.com")
                .setPassword("password123")
                .setName("John Doe")
                .setNik("1234567890")
                .setAddress("123 Blue St")
                .setWorkingAddress("123 Main St");

        Exception exception = assertThrows(IllegalStateException.class, builder::build);
        assertEquals("Required fields are missing", exception.getMessage());
    }
}