package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;
import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ChatMessageRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Test
    void testFindByChatRoomRoomId() {
        // Setup
        ChatRoom room1 = new ChatRoom("room-1", "pac-1", "care-1");
        ChatRoom room2 = new ChatRoom("room-2", "pac-2", "care-2");
        entityManager.persist(room1);
        entityManager.persist(room2);

        ChatMessage message1 = new ChatMessage("user1", "user2", "Hello", LocalDateTime.now(), room1);
        ChatMessage message2 = new ChatMessage("user1", "user2", "Hi", LocalDateTime.now(), room1);
        ChatMessage message3 = new ChatMessage("user3", "user4", "Hey", LocalDateTime.now(), room2);
        
        entityManager.persist(message1);
        entityManager.persist(message2);
        entityManager.persist(message3);
        entityManager.flush();

        // Execute
        List<ChatMessage> foundMessages = chatMessageRepository.findByChatRoomRoomId("room-1");

        // Verify
        assertEquals(2, foundMessages.size());
        assertTrue(foundMessages.stream().allMatch(m -> m.getChatRoom().getRoomId().equals("room-1")));
    }

    @Test
    void testFindByChatRoomRoomId_NoResults() {
        List<ChatMessage> foundMessages = chatMessageRepository.findByChatRoomRoomId("non-existent-room");
        assertTrue(foundMessages.isEmpty());
    }

    @Test
    void testCrudOperations() {
        // Create
        ChatRoom room = new ChatRoom("test-room", "pac-1", "care-1");
        entityManager.persist(room);
        
        ChatMessage message = new ChatMessage("sender", "receiver", "content", LocalDateTime.now(), room);
        ChatMessage saved = chatMessageRepository.save(message);
        
        // Read
        ChatMessage found = chatMessageRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("content", found.getContent());
        
        // Update
        found.setContent("updated content");
        chatMessageRepository.save(found);
        ChatMessage updated = chatMessageRepository.findById(saved.getId()).orElse(null);
        assertEquals("updated content", updated.getContent());
        
        // Delete
        chatMessageRepository.delete(updated);
        assertFalse(chatMessageRepository.findById(saved.getId()).isPresent());
    }
}