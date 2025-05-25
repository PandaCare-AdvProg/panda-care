package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ChatRoomRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @BeforeEach
    void setUp() {
        // Clear any existing data
        entityManager.clear();
    }

    @Test
    void testFindByPacilianIdAndCaregiverId() {
        // Setup
        ChatRoom room1 = new ChatRoom("room-1", "pac-1", "care-1");
        ChatRoom room2 = new ChatRoom("room-2", "pac-1", "care-2");
        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.flush();

        // Execute
        ChatRoom found = chatRoomRepository.findByPacilianIdAndCaregiverId("pac-1", "care-1");

        // Verify
        assertNotNull(found);
        assertEquals("room-1", found.getRoomId());
    }

    @Test
    void testFindByPacilianId() {
        // Setup
        ChatRoom room1 = new ChatRoom("room-1", "pac-1", "care-1");
        ChatRoom room2 = new ChatRoom("room-2", "pac-1", "care-2");
        ChatRoom room3 = new ChatRoom("room-3", "pac-2", "care-1");
        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.persist(room3);
        entityManager.flush();

        // Execute
        List<ChatRoom> found = chatRoomRepository.findByPacilianId("pac-1");

        // Verify
        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(r -> r.getPacilianId().equals("pac-1")));
    }

    @Test
    void testFindByCaregiverId() {
        // Setup
        ChatRoom room1 = new ChatRoom("room-1", "pac-1", "care-1");
        ChatRoom room2 = new ChatRoom("room-2", "pac-2", "care-1");
        ChatRoom room3 = new ChatRoom("room-3", "pac-1", "care-2");
        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.persist(room3);
        entityManager.flush();

        // Execute
        List<ChatRoom> found = chatRoomRepository.findByCaregiverId("care-1");

        // Verify
        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(r -> r.getCaregiverId().equals("care-1")));
    }
}

   