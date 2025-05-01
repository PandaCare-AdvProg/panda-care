package id.ac.ui.cs.advprog.pandacare.model;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.ui.cs.advprog.pandacare.enums.Role;
import org.junit.jupiter.api.Test;

public class DoctorTest {

    @Test
    public void testDoctorConstructor() {
        String email = "doc@example.com";
        String password = "pass";
        String name = "John Doe";
        String nik = "NIK001";
        String address = "Address1";
        String workingAddress = "Address2";
        String phonenum = "123456789";
        Role role = Role.DOCTOR;
        String specialty = "Cardiology";

        Doctor doctor = new Doctor(email, password, name, nik, address, workingAddress, phonenum, role, specialty);

        assertNotNull(doctor);
        assertEquals(email, doctor.getEmail());
        assertEquals(password, doctor.getPassword());
        assertEquals(name, doctor.getName());
        assertEquals(nik, doctor.getNik());
        assertEquals(address, doctor.getAddress());
        assertEquals(workingAddress, doctor.getWorkingAddress());
        assertEquals(phonenum, doctor.getPhonenum());
        assertEquals(role, doctor.getRole());
        assertEquals(specialty, doctor.getSpecialty());
    }

    @Test
    public void testEqualsAndHashCode() {
        Doctor doctor1 = new Doctor("doc@example.com", "pass", "John Doe", "NIK001", "Address1", "Address2", "123456789", Role.DOCTOR, "Cardiology");
        Doctor doctor2 = new Doctor("doc@example.com", "pass", "John Doe", "NIK001", "Address1", "Address2", "123456789", Role.DOCTOR, "Cardiology");

        // The following assertions expect that equals() and hashCode() are properly overridden.
        assertEquals(doctor1, doctor2, "Two doctors with the same attributes should be equal");
        assertEquals(doctor1.hashCode(), doctor2.hashCode(), "Hash codes should match for equal objects");
    }

    @Test
    public void testNotEqualsWhenDifferent() {
        // Create two Doctor objects with one attribute different
        Doctor doctor1 = new Doctor("doc@example.com", "pass", "John Doe", "NIK001", "Address1", "Address2","123456789", Role.DOCTOR,"Cardiology");
        Doctor doctor2 = new Doctor("different@example.com", "pass", "John Doe", "NIK001", "Address1", "Address2", "123456789", Role.DOCTOR, "Cardiology");

        assertNotEquals(doctor1, doctor2, "Doctors with different emails should not be equal");
    }

    @Test
    public void testToString() {
        Doctor doctor = new Doctor("doc@example.com", "pass", "John Doe", "NIK001", "Address1", "Address2", "123456789", Role.DOCTOR, "Cardiology");
        String toString = doctor.toString();

        // Check that key parts of the Doctor's information appear in the output.
        assertTrue(toString.contains("doc@example.com"), "toString should contain the email");
        assertTrue(toString.contains("John Doe"), "toString should contain the name");
        assertTrue(toString.contains("Cardiology"), "toString should contain the specialty");
    }
}
