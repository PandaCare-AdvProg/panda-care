package id.ac.ui.cs.advprog.pandacare.model;

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
        sender = new User();
        sender.setId(100L);
        sender.setRole(Role.DOCTOR);

        receiver = new User();
        receiver.setId(200L);
        receiver.setRole(Role.PATIENT);

        chatMessage = ChatMessage.builder()
                .id(1L)
                .sender(sender)
                .receiver(receiver)
                .senderRole(Role.DOCTOR)
                .createdAt(testTime)
                .build();

        chatMessage.setContent("Test message");
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
        assertFalse(chatMessage.isEdited());
        assertFalse(chatMessage.isDeleted());
    }

    @Test
    void testEditMessage() {
        chatMessage.setContent("Updated message");
        assertEquals("Updated message", chatMessage.getContent());
        assertTrue(chatMessage.isEdited());
    }

    @Test
    void testDeleteMessage() {
        chatMessage.markAsDeleted();
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
        chatMessage.setContent("");  // already initialized from setUp
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
        newMessage.onUpdate();

        assertNotNull(newMessage.getCreatedAt());
        assertNotNull(newMessage.getUpdatedAt());
    }

    @Test
    void testObserverNotification() {
        StringBuilder log = new StringBuilder();
        ChatObserver observer = (msg, event) -> log.append(event);

        chatMessage.addObserver(observer);
        chatMessage.setContent("Another update");

        assertTrue(log.toString().contains("Content updated"));

        chatMessage.markAsDeleted();
        assertTrue(log.toString().contains("Message deleted"));
    }
}
