package id.ac.ui.cs.advprog.pandacare.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;

public interface DoctorRepository1 extends JpaRepository<Doctor, Long> {
    List<Doctor> findByNameContainingIgnoreCase(String name);
    List<Doctor> findBySpecialtyContainingIgnoreCase(String specialty);
}
