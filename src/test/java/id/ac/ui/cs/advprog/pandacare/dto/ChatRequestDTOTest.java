package id.ac.ui.cs.advprog.pandacare.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatRequestTest {

    @Test
    void testChatRequestBuilder() {
        ChatRequest request = ChatRequest.builder()
                .roomId(1L)
                .messageId(2L)
                .content("Hello, doctor")
                .build();
        
        assertThat(request.getRoomId()).isEqualTo(1L);
        assertThat(request.getMessageId()).isEqualTo(2L);
        assertThat(request.getContent()).isEqualTo("Hello, doctor");
    }
    
    @Test
    void testChatRequestNoArgsConstructorAndSetters() {
        ChatRequest request = new ChatRequest();
        request.setRoomId(1L);
        request.setMessageId(2L);
        request.setContent("Hello, doctor");
        
        assertThat(request.getRoomId()).isEqualTo(1L);
        assertThat(request.getMessageId()).isEqualTo(2L);
        assertThat(request.getContent()).isEqualTo("Hello, doctor");
    }
    
    @Test
    void testChatRequestAllArgsConstructor() {
        ChatRequest request = new ChatRequest(1L, 2L, "Hello, doctor");
        
        assertThat(request.getRoomId()).isEqualTo(1L);
        assertThat(request.getMessageId()).isEqualTo(2L);
        assertThat(request.getContent()).isEqualTo("Hello, doctor");
    }
    
    @Test
    void testChatRequestEquality() {
        ChatRequest request1 = ChatRequest.builder()
                .roomId(1L)
                .messageId(2L)
                .content("Hello")
                .build();
                
        ChatRequest request2 = ChatRequest.builder()
                .roomId(1L)
                .messageId(2L)
                .content("Hello")
                .build();
                
        ChatRequest request3 = ChatRequest.builder()
                .roomId(3L)
                .messageId(2L)
                .content("Hello")
                .build();
        
        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1.hashCode()).isNotEqualTo(request3.hashCode());
    }
}