package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.enums.ConsultationStatus;
import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ConsultationRepositoryTest {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    @DisplayName("Should save and retrieve Consultation by ID")
    void testSaveAndFindById() {
        Consultation consultation = createConsultation("doc1@example.com", "pat1@example.com");
        consultation = consultationRepository.save(consultation);

        Optional<Consultation> found = consultationRepository.findById(consultation.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getDoctor().getId()).isEqualTo(consultation.getDoctor().getId());
        assertThat(found.get().getPatient().getId()).isEqualTo(consultation.getPatient().getId());
    }

    @Test
    @DisplayName("Should delete Consultation by ID")
    void testDeleteById() {
        Consultation consultation = createConsultation("doc2@example.com", "pat2@example.com");
        consultation = consultationRepository.save(consultation);

        consultationRepository.deleteById(consultation.getId());

        Optional<Consultation> deleted = consultationRepository.findById(consultation.getId());
        assertThat(deleted).isNotPresent();
    }

    @Test
    @DisplayName("Should find Consultations by Doctor ID")
    void testFindByDoctorId() {
        Doctor doctor = createDoctor("doc3@example.com");
        Patient patient1 = createPatient("pat3a@example.com");
        Patient patient2 = createPatient("pat3b@example.com");
        consultationRepository.save(new Consultation(doctor, patient1, null, LocalTime.now(), null, null, null));
        consultationRepository.save(new Consultation(doctor, patient2, null, LocalTime.now(), null, null, null));

        Doctor anotherDoctor = createDoctor("doc4@example.com");
        Patient anotherPatient = createPatient("pat4@example.com");
        consultationRepository.save(new Consultation(anotherDoctor, anotherPatient, null, LocalTime.now(), null, null, null));

        List<Consultation> results = consultationRepository.findByDoctorId(doctor.getId());

        assertThat(results).hasSize(2);
        assertThat(results).allMatch(c -> c.getDoctor().getId().equals(doctor.getId()));
    }

    @Test
    @DisplayName("Should find Consultations by Patient ID")
    void testFindByPatientId() {
        Patient patient = createPatient("pat5@example.com");
        Doctor doctor1 = createDoctor("doc5a@example.com");
        Doctor doctor2 = createDoctor("doc5b@example.com");
        consultationRepository.save(new Consultation(doctor1, patient, null, LocalTime.now(), null, null, null));
        consultationRepository.save(new Consultation(doctor2, patient, null, LocalTime.now(), null, null, null));

        Patient anotherPatient = createPatient("pat6@example.com");
        Doctor anotherDoctor = createDoctor("doc6@example.com");
        consultationRepository.save(new Consultation(anotherDoctor, anotherPatient, null, LocalTime.now(), null, null, null));

        List<Consultation> results = consultationRepository.findByPatientId(patient.getId());

        assertThat(results).hasSize(2);
        assertThat(results).allMatch(c -> c.getPatient().getId().equals(patient.getId()));
    }

    private Consultation createConsultation(String doctorEmail, String patientEmail) {
        Doctor doctor = createDoctor(doctorEmail);
        Patient patient = createPatient(patientEmail);

        Consultation consultation = new Consultation();
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        consultation.setScheduledTime(LocalTime.now());
        consultation.setStatus(ConsultationStatus.PENDING);
        return consultation;
    }

    private Doctor createDoctor(String email) {
        Doctor doctor = new Doctor();
        doctor.setEmail(email);
        doctor.setSpecialty("General");
        doctor.setWorkingAddress("Clinic");
        return doctorRepository.save(doctor);
    }

    private Patient createPatient(String email) {
        Patient patient = new Patient();
        patient.setEmail(email);
        patient.setName("Test Patient");
        return patientRepository.save(patient);
    }
}