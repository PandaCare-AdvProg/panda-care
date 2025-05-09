package id.ac.ui.cs.advprog.pandacare.dto;

import id.ac.ui.cs.advprog.pandacare.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private Long id;
    private Long roomId;
    private Long senderId;
    private String senderName;
    private Role senderRole;
    private String content;
    private boolean edited;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}