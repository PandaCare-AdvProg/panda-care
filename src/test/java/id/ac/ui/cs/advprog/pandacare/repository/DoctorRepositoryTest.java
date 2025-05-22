package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.dto.DoctorDTO;
import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    private Doctor doctor;

    @BeforeEach
    void setup() {
        doctor = new Doctor(
                "doc@example.com",
                "pass123",
                "John Doe",
                "1234567890",
                "Jakarta",
                "RSCM",
                "08123456789",
                Role.DOCTOR,
                "Cardiology"
        );
        doctorRepository.save(doctor);
    }

    @Test
    void testFindDoctorDTOById() {
        Optional<DoctorDTO> dto = doctorRepository.findDoctorDTOById(doctor.getId());

        assertThat(dto).isPresent();
        assertThat(dto.get().getId()).isEqualTo(doctor.getId());
        assertThat(dto.get().getSpecialty()).isEqualTo("Cardiology");
        assertThat(dto.get().getWorking_adress()).isEqualTo("RSCM");
    }

    @Test
    void testFindDoctorDTOByEmail() {
        Optional<DoctorDTO> dto = doctorRepository.findDoctorDTOByEmail("doc@example.com");

        assertThat(dto).isPresent();
        assertThat(dto.get().getSpecialty()).isEqualTo("Cardiology");
        assertThat(dto.get().getWorking_adress()).isEqualTo("RSCM");
    }

    @Test
    void testFindDoctorByEmail() {
        Optional<Doctor> found = doctorRepository.findDoctorByEmail("doc@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getSpecialty()).isEqualTo("Cardiology");
        assertThat(found.get().getWorkingAddress()).isEqualTo("RSCM");
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        List<Doctor> result = doctorRepository.findByNameContainingIgnoreCase("john");

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getName()).containsIgnoringCase("john");
    }

    @Test
    void testFindBySpecialtyContainingIgnoreCase() {
        List<Doctor> result = doctorRepository.findBySpecialtyContainingIgnoreCase("cardio");

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getSpecialty()).containsIgnoringCase("cardio");
    }
}
