package id.ac.ui.cs.advprog.pandacare.dto;

import id.ac.ui.cs.advprog.pandacare.enums.ConsultationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationHistoryDTO {
    private Long id;
    private String doctorName;
    private String patientName;
    private DayOfWeek dayOfWeek;
    private LocalTime scheduledTime;
    private String notes;
    private ConsultationStatus status;
    private LocalDateTime createdAt;
}