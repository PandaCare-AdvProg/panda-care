package id.ac.ui.cs.advprog.pandacare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditChatRequest {
    private String roomId;
    private String messageId;
    private String content;
}