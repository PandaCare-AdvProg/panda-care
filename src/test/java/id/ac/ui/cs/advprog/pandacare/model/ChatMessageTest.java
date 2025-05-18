package id.ac.ui.cs.advprog.pandacare.model;

import id.ac.ui.cs.advprog.pandacare.model.User;

import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.observer.ChatObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ChatMessageTest {

    private ChatMessage chatMessage;
    private User sender;
    private User receiver;
    private final LocalDateTime testTime = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Create test users
        sender = new User();
        sender.setId(100L);
        sender.setRole(Role.DOCTOR);

        receiver = new User();
        receiver.setId(200L);
        receiver.setRole(Role.PATIENT);

        chatMessage = new ChatMessage();
        chatMessage.setId(1L);
        chatMessage.setSender(sender);
        chatMessage.setReceiver(receiver);
        chatMessage.setSenderRole(Role.DOCTOR);
        chatMessage.setContent("Test message");
        chatMessage.setCreatedAt(testTime);

    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, chatMessage.getId());
        assertEquals(sender, chatMessage.getSender());
        assertEquals(receiver, chatMessage.getReceiver());
        assertEquals(100L, chatMessage.getSender().getId());
        assertEquals(200L, chatMessage.getReceiver().getId());
        assertEquals(Role.DOCTOR, chatMessage.getSenderRole());
        assertEquals("Test message", chatMessage.getContent());
        assertEquals(testTime, chatMessage.getCreatedAt());
        assertNull(chatMessage.getUpdatedAt());
        assertFalse(chatMessage.isEdited()); // Fixed: should be false initially
        assertFalse(chatMessage.isDeleted());
    }

    @Test
    void testEditMessage() {
        LocalDateTime updateTime = testTime.plusHours(1);
        chatMessage.setContent("Updated message");
        chatMessage.setUpdatedAt(updateTime);

        assertEquals("Updated message", chatMessage.getContent());
        assertTrue(chatMessage.isEdited()); // Now true after edit
        assertEquals(updateTime, chatMessage.getUpdatedAt());
    }

    @Test
    void testDeleteMessage() {
        chatMessage.markAsDeleted(); // Using proper method instead of direct setter
        assertTrue(chatMessage.isDeleted());
    }

    @Test
    void testSenderRoleValidation() {
        chatMessage.setSenderRole(Role.PATIENT);
        assertEquals(Role.PATIENT, chatMessage.getSenderRole());

        chatMessage.setSenderRole(Role.DOCTOR);
        assertEquals(Role.DOCTOR, chatMessage.getSenderRole());
    }

    @Test
    void testContentEdgeCases() {
        chatMessage.setContent("");
        assertEquals("", chatMessage.getContent());

        chatMessage.setContent(null);
        assertNull(chatMessage.getContent());
    }


    @Test
    void testNullSafety() {
        chatMessage.setSender(null);
        assertNull(chatMessage.getSender());
        assertNull(chatMessage.getSenderId());

        chatMessage.setReceiver(null);
        assertNull(chatMessage.getReceiver());
        assertNull(chatMessage.getReceiverId());
    }

    @Test
    void testTimestamps() {
        ChatMessage newMessage = new ChatMessage();
        assertNull(newMessage.getCreatedAt());
        assertNull(newMessage.getUpdatedAt());

        newMessage.setCreatedAt(testTime);
        newMessage.setUpdatedAt(testTime.plusMinutes(1));

        assertNotNull(newMessage.getCreatedAt());
        assertNotNull(newMessage.getUpdatedAt());
    }


}