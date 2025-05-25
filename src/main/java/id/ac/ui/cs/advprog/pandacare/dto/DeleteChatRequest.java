package id.ac.ui.cs.advprog.pandacare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteChatRequest {
    private String roomId;
    private String messageId;
}
