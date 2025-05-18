package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.dto.DoctorDTO;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByNameContainingIgnoreCase(String name);

    List<Doctor> findBySpecialtyContainingIgnoreCase(String specialty);

    @Query("select new id.ac.ui.cs.advprog.pandacare.dto.DoctorDTO(d.id, d.specialty, d.workingAddress) " +
            "from Doctor d where d.id = :id")
    Optional<DoctorDTO> findDoctorDTOById(@Param("id") Long id);

    @Query("select new id.ac.ui.cs.advprog.pandacare.dto.DoctorDTO(d.id, d.specialty, d.workingAddress) " +
            "from Doctor d where d.email = :email")
    Optional<DoctorDTO> findDoctorDTOByEmail(@Param("email") String email);

    @Query("select d from Doctor d where d.email = :email")
    Optional<Doctor> findDoctorByEmail(@Param("email") String email);
}
