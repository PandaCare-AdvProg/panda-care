package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;
import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import id.ac.ui.cs.advprog.pandacare.model.User;
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
    void testFindByRoomOrderByCreatedAtAsc() {
        User doctor = new User();
        User patient = new User();
        entityManager.persist(doctor);
        entityManager.persist(patient);

        ChatRoom room = new ChatRoom();
        room.setDoctor(doctor);
        room.setPatient(patient);
        entityManager.persist(room);

        ChatMessage message1 = ChatMessage.builder()
                .room(room)
                .sender(doctor)
                .receiver(patient)
                .content("Hello")
                .createdAt(LocalDateTime.now().minusHours(1))
                .build();

        ChatMessage message2 = ChatMessage.builder()
                .room(room)
                .sender(patient)
                .receiver(doctor)
                .content("Hi")
                .createdAt(LocalDateTime.now())
                .build();

        entityManager.persist(message1);
        entityManager.persist(message2);
        entityManager.flush();

        List<ChatMessage> messages = chatMessageRepository.findByRoomOrderByCreatedAtAsc(room);
        assertEquals(2, messages.size());
        assertEquals("Hello", messages.get(0).getContent());
        assertEquals("Hi", messages.get(1).getContent());
    }

    @Test
    void testFindByRoomAndDeletedFalseOrderByCreatedAtAsc() {
        User doctor = new User();
        User patient = new User();
        entityManager.persist(doctor);
        entityManager.persist(patient);

        ChatRoom room = new ChatRoom();
        room.setDoctor(doctor);
        room.setPatient(patient);
        entityManager.persist(room);

        ChatMessage message1 = ChatMessage.builder()
                .room(room)
                .sender(doctor)
                .receiver(patient)
                .content("Hello")
                .deleted(false)
                .createdAt(LocalDateTime.now().minusHours(1))
                .build();

        ChatMessage message2 = ChatMessage.builder()
                .room(room)
                .sender(patient)
                .receiver(doctor)
                .content("Hi")
                .deleted(true)
                .createdAt(LocalDateTime.now())
                .build();

        entityManager.persist(message1);
        entityManager.persist(message2);
        entityManager.flush();

        List<ChatMessage> messages = chatMessageRepository.findByRoomAndDeletedFalseOrderByCreatedAtAsc(room);
        assertEquals(1, messages.size());
        assertEquals("Hello", messages.get(0).getContent());
    }

    @Test
    void testFindBySender() {
        User doctor = new User();
        User patient = new User();
        entityManager.persist(doctor);
        entityManager.persist(patient);

        ChatMessage message1 = ChatMessage.builder()
                .sender(doctor)
                .receiver(patient)
                .content("Hello")
                .build();

        ChatMessage message2 = ChatMessage.builder()
                .sender(patient)
                .receiver(doctor)
                .content("Hi")
                .build();

        entityManager.persist(message1);
        entityManager.persist(message2);
        entityManager.flush();

        List<ChatMessage> messages = chatMessageRepository.findBySender(doctor);
        assertEquals(1, messages.size());
        assertEquals("Hello", messages.get(0).getContent());
    }

    @Test
    void testFindByReceiver() {
        User doctor = new User();
        User patient = new User();
        entityManager.persist(doctor);
        entityManager.persist(patient);

        ChatMessage message1 = ChatMessage.builder()
                .sender(doctor)
                .receiver(patient)
                .content("Hello")
                .build();

        ChatMessage message2 = ChatMessage.builder()
                .sender(patient)
                .receiver(doctor)
                .content("Hi")
                .build();

        entityManager.persist(message1);
        entityManager.persist(message2);
        entityManager.flush();

        List<ChatMessage> messages = chatMessageRepository.findByReceiver(patient);
        assertEquals(1, messages.size());
        assertEquals("Hello", messages.get(0).getContent());
    }
}