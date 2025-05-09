package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    void deleteById(Long consultationId);
    List<Consultation> findByDoctorId(Long doctorId);
    List<Consultation> findByPatientId(Long patientId);
}