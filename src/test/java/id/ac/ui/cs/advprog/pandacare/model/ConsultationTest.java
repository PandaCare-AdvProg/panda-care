package id.ac.ui.cs.advprog.pandacare.model;

import id.ac.ui.cs.advprog.pandacare.enums.ConsultationStatus;
import id.ac.ui.cs.advprog.pandacare.observer.ConsultationObserver;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

    @Test
    void addObserver_shouldAddObserver() {
        Consultation consultation = new Consultation();
        ConsultationObserver observer = mock(ConsultationObserver.class);

        consultation.addObserver(observer);

        assertThat(consultation.getObservers()).contains(observer);
    }

    @Test
    void addObserver_shouldNotAddDuplicateObserver() {
        Consultation consultation = new Consultation();
        ConsultationObserver observer = mock(ConsultationObserver.class);

        consultation.addObserver(observer);
        consultation.addObserver(observer);

        assertThat(consultation.getObservers()).hasSize(1);
    }

    @Test
    void removeObserver_shouldRemoveObserver() {
        Consultation consultation = new Consultation();
        ConsultationObserver observer = mock(ConsultationObserver.class);

        consultation.addObserver(observer);
        consultation.removeObserver(observer);

        assertThat(consultation.getObservers()).doesNotContain(observer);
    }

    @Test
    void notifyObservers_shouldNotifyAllObservers() {
        Consultation consultation = new Consultation();
        ConsultationObserver observer1 = mock(ConsultationObserver.class);
        ConsultationObserver observer2 = mock(ConsultationObserver.class);

        consultation.addObserver(observer1);
        consultation.addObserver(observer2);

        consultation.setStatus(ConsultationStatus.APPROVED);

        verify(observer1).update(eq(consultation), eq("Status changed to APPROVED"));
        verify(observer2).update(eq(consultation), eq("Status changed to APPROVED"));
    }

    @Test
    void notifyObservers_shouldNotFailWithNoObservers() {
        Consultation consultation = new Consultation();

        consultation.setStatus(ConsultationStatus.APPROVED);

        // No exception should be thrown
        assertThat(consultation.getObservers()).isEmpty();
    }
}