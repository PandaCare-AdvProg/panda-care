package id.ac.ui.cs.advprog.pandacare.model;

import id.ac.ui.cs.advprog.pandacare.enums.ConsultationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ConsultationTest {

    @Test
    void consultationCreation_shouldSetDefaultPending() {
        Doctor doctor = new Doctor();
        Patient patient = new Patient();
        LocalDateTime scheduledTime = LocalDateTime.now().plusDays(1);

        Consultation consultation = new Consultation(doctor, patient, scheduledTime, "http://meeting.url", "Initial notes");

        assertThat(consultation.getStatus()).isEqualTo(ConsultationStatus.PENDING);
        assertThat(consultation.getDoctor()).isEqualTo(doctor);
        assertThat(consultation.getPatient()).isEqualTo(patient);
        assertThat(consultation.getScheduledTime()).isEqualTo(scheduledTime);
        assertThat(consultation.getMeetingUrl()).isEqualTo("http://meeting.url");
        assertThat(consultation.getNotes()).isEqualTo("Initial notes");
    }

    @Test
    void setStatus_shouldUpdateStatus() {
        Consultation consultation = new Consultation();
        consultation.setStatus(ConsultationStatus.APPROVED);

        assertThat(consultation.getStatus()).isEqualTo(ConsultationStatus.APPROVED);
    }

    @Test
    void setNotes_shouldUpdateNotes() {
        Consultation consultation = new Consultation();
        consultation.setNotes("Updated notes");

        assertThat(consultation.getNotes()).isEqualTo("Updated notes");
    }

    @Test
    void setMeetingUrl_shouldUpdateMeetingUrl() {
        Consultation consultation = new Consultation();
        consultation.setMeetingUrl("http://new.meeting.url");

        assertThat(consultation.getMeetingUrl()).isEqualTo("http://new.meeting.url");
    }
}