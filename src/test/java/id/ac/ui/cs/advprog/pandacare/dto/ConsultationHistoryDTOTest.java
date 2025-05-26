package id.ac.ui.cs.advprog.pandacare.dto;

import id.ac.ui.cs.advprog.pandacare.enums.ConsultationStatus;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ConsultationHistoryDTOTest {

    @Test
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        ConsultationHistoryDTO dto = ConsultationHistoryDTO.builder()
                .id(1L)
                .doctorName("Dr. Smith")
                .patientName("John Doe")
                .dayOfWeek(DayOfWeek.MONDAY)
                .scheduledTime(LocalTime.of(10, 0))
                .notes("Regular checkup")
                .status(ConsultationStatus.COMPLETED)
                .createdAt(now)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals("Dr. Smith", dto.getDoctorName());
        assertEquals("John Doe", dto.getPatientName());
        assertEquals(DayOfWeek.MONDAY, dto.getDayOfWeek());
        assertEquals(LocalTime.of(10, 0), dto.getScheduledTime());
        assertEquals("Regular checkup", dto.getNotes());
        assertEquals(ConsultationStatus.COMPLETED, dto.getStatus());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    void testSettersAndGetters() {
        ConsultationHistoryDTO dto = new ConsultationHistoryDTO();
        LocalDateTime now = LocalDateTime.now();
        
        dto.setId(2L);
        dto.setDoctorName("Dr. Jones");
        dto.setPatientName("Jane Smith");
        dto.setDayOfWeek(DayOfWeek.FRIDAY);
        dto.setScheduledTime(LocalTime.of(14, 30));
        dto.setNotes("Follow-up");
        dto.setStatus(ConsultationStatus.RESCHEDULED);
        dto.setCreatedAt(now);

        assertEquals(2L, dto.getId());
        assertEquals("Dr. Jones", dto.getDoctorName());
        assertEquals("Jane Smith", dto.getPatientName());
        assertEquals(DayOfWeek.FRIDAY, dto.getDayOfWeek());
        assertEquals(LocalTime.of(14, 30), dto.getScheduledTime());
        assertEquals("Follow-up", dto.getNotes());
        assertEquals(ConsultationStatus.RESCHEDULED, dto.getStatus());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsultationHistoryDTO consultation1 = ConsultationHistoryDTO.builder()
                .id(1L)
                .doctorName("Dr. Smith")
                .patientName("John Doe")
                .build();
        
        ConsultationHistoryDTO consultation2 = ConsultationHistoryDTO.builder()
                .id(1L)
                .doctorName("Dr. Smith")
                .patientName("John Doe")
                .build();
        
        ConsultationHistoryDTO consultation3 = ConsultationHistoryDTO.builder()
                .id(2L)
                .doctorName("Dr. Smith")
                .patientName("John Doe")
                .build();

        assertEquals(consultation1, consultation2);
        assertNotEquals(consultation1, consultation3);
        assertEquals(consultation1.hashCode(), consultation2.hashCode());
    }
}