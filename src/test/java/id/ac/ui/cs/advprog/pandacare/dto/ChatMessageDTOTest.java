package id.ac.ui.cs.advprog.pandacare.dto;

import id.ac.ui.cs.advprog.pandacare.enums.Role;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ChatMessageDTOTest {

    @Test
    void testChatMessageDTOBuilder() {
        LocalDateTime now = LocalDateTime.now();
        
        ChatMessageDTO dto = ChatMessageDTO.builder()
                .id(1L)
                .roomId(2L)
                .senderId(3L)
                .senderName("Dr. Smith")
                .senderRole(Role.DOCTOR)
                .content("Hello, how are you?")
                .edited(true)
                .deleted(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getRoomId()).isEqualTo(2L);
        assertThat(dto.getSenderId()).isEqualTo(3L);
        assertThat(dto.getSenderName()).isEqualTo("Dr. Smith");
        assertThat(dto.getSenderRole()).isEqualTo(Role.DOCTOR);
        assertThat(dto.getContent()).isEqualTo("Hello, how are you?");
        assertThat(dto.isEdited()).isTrue();
        assertThat(dto.isDeleted()).isFalse();
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }
    
    @Test
    void testChatMessageDTONoArgsConstructorAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(1L);
        dto.setRoomId(2L);
        dto.setSenderId(3L);
        dto.setSenderName("Dr. Smith");
        dto.setSenderRole(Role.DOCTOR);
        dto.setContent("Hello, how are you?");
        dto.setEdited(true);
        dto.setDeleted(false);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getRoomId()).isEqualTo(2L);
        assertThat(dto.getSenderId()).isEqualTo(3L);
        assertThat(dto.getSenderName()).isEqualTo("Dr. Smith");
        assertThat(dto.getSenderRole()).isEqualTo(Role.DOCTOR);
        assertThat(dto.getContent()).isEqualTo("Hello, how are you?");
        assertThat(dto.isEdited()).isTrue();
        assertThat(dto.isDeleted()).isFalse();
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }
    
    @Test
    void testChatMessageDTOEquality() {
        ChatMessageDTO dto1 = ChatMessageDTO.builder()
                .id(1L)
                .roomId(2L)
                .senderId(3L)
                .content("Hello")
                .build();
                
        ChatMessageDTO dto2 = ChatMessageDTO.builder()
                .id(1L)
                .roomId(2L)
                .senderId(3L)
                .content("Hello")
                .build();
                
        ChatMessageDTO dto3 = ChatMessageDTO.builder()
                .id(2L)
                .roomId(2L)
                .senderId(3L)
                .content("Hello")
                .build();
        
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1).isNotEqualTo(dto3);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1.hashCode()).isNotEqualTo(dto3.hashCode());
    }
}