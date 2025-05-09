package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.dto.DoctorDTO;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

@Test
@DisplayName("Should find DoctorDTO by id")
void testFindDoctorDTOById() {
    Doctor doctor = new Doctor();
    doctor.setSpecialty("Cardiology");
    doctor.setWorkingAddress("123 Panda St");
    doctor.setEmail("doc1@panda.com");
    doctor.setRole(id.ac.ui.cs.advprog.pandacare.enums.Role.DOCTOR); 
    doctor = doctorRepository.save(doctor);

    Optional<DoctorDTO> dto = doctorRepository.findDoctorDTOById(doctor.getId());
    assertThat(dto).isPresent();
    assertThat(dto.get().getId()).isEqualTo(doctor.getId());
    assertThat(dto.get().getSpecialty()).isEqualTo("Cardiology");
    assertThat(dto.get().getWorking_adress()).isEqualTo("123 Panda St");
}

@Test
@DisplayName("Should find DoctorDTO by email")
void testFindDoctorDTOByEmail() {
    Doctor doctor = new Doctor();
    doctor.setSpecialty("Dermatology");
    doctor.setWorkingAddress("456 Bamboo Ave");
    doctor.setEmail("doc2@panda.com");
    doctor.setRole(id.ac.ui.cs.advprog.pandacare.enums.Role.DOCTOR);
    doctor = doctorRepository.save(doctor);

    Optional<DoctorDTO> dto = doctorRepository.findDoctorDTOByEmail("doc2@panda.com");
    assertThat(dto).isPresent();
    assertThat(dto.get().getSpecialty()).isEqualTo("Dermatology");
    assertThat(dto.get().getWorking_adress()).isEqualTo("456 Bamboo Ave");
}

    @Test
    @DisplayName("Should find Doctor by email")
    void testFindDoctorByEmail() {
        Doctor doctor = new Doctor();
        doctor.setSpecialty("Neurology");
        doctor.setWorkingAddress("789 Bear Rd");
        doctor.setEmail("doc3@panda.com");
        doctor.setRole(id.ac.ui.cs.advprog.pandacare.enums.Role.DOCTOR);
        doctor = doctorRepository.save(doctor);

        Optional<Doctor> found = doctorRepository.findDoctorByEmail("doc3@panda.com");
        assertThat(found).isPresent();
        assertThat(found.get().getSpecialty()).isEqualTo("Neurology");
        assertThat(found.get().getWorkingAddress()).isEqualTo("789 Bear Rd");
    }
}