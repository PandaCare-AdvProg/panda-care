package id.ac.ui.cs.advprog.pandacare.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DoctorDTOTest {

    @Test
    void testConstructorAndGetters() {
        Long id = 1L;
        String specialty = "Cardiology";
        String workingAddress = "123 Panda St";

        DoctorDTO doctor = new DoctorDTO(id, specialty, workingAddress);

        assertEquals(id, doctor.getId());
        assertEquals(specialty, doctor.getSpecialty());
        assertEquals(workingAddress, doctor.getWorking_adress());
    }
}