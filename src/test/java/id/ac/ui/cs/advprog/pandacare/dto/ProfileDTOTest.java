package id.ac.ui.cs.advprog.pandacare.dto;

import id.ac.ui.cs.advprog.pandacare.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileDTOTest {

    @Test
    void testBuilder() {
        ProfileDTO profile = ProfileDTO.builder()
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .nik("1234567890")
                .address("Test Address")
                .phonenum("123456789")
                .role(Role.PATIENT)
                .medicalHistory("No significant history")
                .build();

        assertEquals(1L, profile.getId());
        assertEquals("test@example.com", profile.getEmail());
        assertEquals("Test User", profile.getName());
        assertEquals("1234567890", profile.getNik());
        assertEquals("Test Address", profile.getAddress());
        assertEquals("123456789", profile.getPhonenum());
        assertEquals(Role.PATIENT, profile.getRole());
        assertEquals("No significant history", profile.getMedicalHistory());
        assertNull(profile.getSpecialty()); // Not set for patients
    }

    @Test
    void testDoctorProfileDTO() {
        ProfileDTO profile = ProfileDTO.builder()
                .id(2L)
                .email("doctor@example.com")
                .name("Dr. Test")
                .role(Role.DOCTOR)
                .specialty("Cardiology")
                .workingAddress("Hospital Address")
                .build();

        assertEquals(2L, profile.getId());
        assertEquals("Cardiology", profile.getSpecialty());
        assertEquals("Hospital Address", profile.getWorkingAddress());
        assertNull(profile.getMedicalHistory()); // Not set for doctors
    }

    @Test
    void testSettersAndGetters() {
        ProfileDTO profile = new ProfileDTO();
        profile.setId(3L);
        profile.setEmail("updated@example.com");
        profile.setName("Updated Name");
        profile.setRole(Role.PATIENT);

        assertEquals(3L, profile.getId());
        assertEquals("updated@example.com", profile.getEmail());
        assertEquals("Updated Name", profile.getName());
        assertEquals(Role.PATIENT, profile.getRole());
    }

    @Test
    void testEqualsAndHashCode() {
        ProfileDTO profile1 = ProfileDTO.builder().id(1L).name("Test").build();
        ProfileDTO profile2 = ProfileDTO.builder().id(1L).name("Test").build();
        ProfileDTO profile3 = ProfileDTO.builder().id(2L).name("Test").build();

        assertEquals(profile1, profile2);
        assertNotEquals(profile1, profile3);
        assertEquals(profile1.hashCode(), profile2.hashCode());
    }
} 