package id.ac.ui.cs.advprog.pandacare.model;

import id.ac.ui.cs.advprog.pandacare.Auth.User;
import id.ac.ui.cs.advprog.pandacare.enums.Role;
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

        // Create chat message
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
        chatMessage.setSenderRole(Role.PATIENT);
        assertEquals(Role.PATIENT, chatMessage.getSenderRole());

        chatMessage.setSenderRole(Role.DOCTOR);
        assertEquals(Role.DOCTOR, chatMessage.getSenderRole());
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

    @Test
    void testTimestampAutomation() throws InterruptedException {
        ChatMessage newMessage = new ChatMessage();
        assertNull(newMessage.getCreatedAt());
        assertNull(newMessage.getUpdatedAt());

        newMessage.onCreate();
        assertNotNull(newMessage.getCreatedAt());
        assertEquals(newMessage.getCreatedAt(), newMessage.getUpdatedAt());

        LocalDateTime originalTime = newMessage.getUpdatedAt();
        Thread.sleep(1);

        newMessage.onUpdate();
        assertNotNull(newMessage.getUpdatedAt());
        assertTrue(originalTime.isBefore(newMessage.getUpdatedAt()),
                "Updated time should be after original time");
    }

    @Test
    void testUserRelationships() {
        User newSender = new User();
        newSender.setId(300L);
        newSender.setRole(Role.ADMIN);

        User newReceiver = new User();
        newReceiver.setId(400L);
        newReceiver.setRole(Role.USER);

        chatMessage.setSender(newSender);
        chatMessage.setReceiver(newReceiver);

        assertEquals(300L, chatMessage.getSender().getId());
        assertEquals(Role.ADMIN, chatMessage.getSender().getRole());
        assertEquals(400L, chatMessage.getReceiver().getId());
        assertEquals(Role.USER, chatMessage.getReceiver().getRole());
    }
}