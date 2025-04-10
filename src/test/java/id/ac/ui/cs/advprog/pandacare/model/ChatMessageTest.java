package id.ac.ui.cs.advprog.pandacare.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ChatMessageTest {

    private ChatMessage chatMessage;
    private final LocalDateTime testTime = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        chatMessage = new ChatMessage();
        chatMessage.setId(1L);
        chatMessage.setSenderId(100L);
        chatMessage.setReceiverId(200L);
        chatMessage.setSenderRole("DOCTOR");
        chatMessage.setContent("Test message");
        chatMessage.setCreatedAt(testTime);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, chatMessage.getId());
        assertEquals(100L, chatMessage.getSenderId());
        assertEquals(200L, chatMessage.getReceiverId());
        assertEquals("DOCTOR", chatMessage.getSenderRole());
        assertEquals("Test message", chatMessage.getContent());
        assertEquals(testTime, chatMessage.getCreatedAt());
        assertFalse(chatMessage.isEdited());
        assertFalse(chatMessage.isDeleted());
        assertNull(chatMessage.getUpdatedAt());
    }

    @Test
    void testEditMessage() {
        LocalDateTime updateTime = testTime.plusHours(1);
        chatMessage.setContent("Updated message");
        chatMessage.setEdited(true);
        chatMessage.setUpdatedAt(updateTime);

        assertEquals("Updated message", chatMessage.getContent());
        assertTrue(chatMessage.isEdited());
        assertEquals(updateTime, chatMessage.getUpdatedAt());
    }

    @Test
    void testDeleteMessage() {
        chatMessage.setDeleted(true);
        assertTrue(chatMessage.isDeleted());
    }

    @Test
    void testSenderRoleValidation() {
        chatMessage.setSenderRole("PACILLIAN");
        assertEquals("PACILLIAN", chatMessage.getSenderRole());

        chatMessage.setSenderRole("DOCTOR");
        assertEquals("DOCTOR", chatMessage.getSenderRole());
    }

    @Test
    void testContentEdgeCases() {
        // Test empty content
        chatMessage.setContent("");
        assertEquals("", chatMessage.getContent());

        // Test null content
        chatMessage.setContent(null);
        assertNull(chatMessage.getContent());
    }
}