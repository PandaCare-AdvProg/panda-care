package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import id.ac.ui.cs.advprog.pandacare.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ChatRoomRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    void testFindByDoctor() {
        User doctor = new User();
        User patient1 = new User();
        User patient2 = new User();
        entityManager.persist(doctor);
        entityManager.persist(patient1);
        entityManager.persist(patient2);

        ChatRoom room1 = new ChatRoom();
        room1.setDoctor(doctor);
        room1.setPatient(patient1);

        ChatRoom room2 = new ChatRoom();
        room2.setDoctor(doctor);
        room2.setPatient(patient2);

        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.flush();

        List<ChatRoom> rooms = chatRoomRepository.findByDoctor(doctor);
        assertEquals(2, rooms.size());
    }

    @Test
    void testFindByPatient() {
        User doctor1 = new User();
        User doctor2 = new User();
        User patient = new User();
        entityManager.persist(doctor1);
        entityManager.persist(doctor2);
        entityManager.persist(patient);

        ChatRoom room1 = new ChatRoom();
        room1.setDoctor(doctor1);
        room1.setPatient(patient);

        ChatRoom room2 = new ChatRoom();
        room2.setDoctor(doctor2);
        room2.setPatient(patient);

        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.flush();

        List<ChatRoom> rooms = chatRoomRepository.findByPatient(patient);
        assertEquals(2, rooms.size());
    }

    @Test
    void testFindByDoctorAndPatient() {
        User doctor = new User();
        User patient = new User();
        entityManager.persist(doctor);
        entityManager.persist(patient);

        ChatRoom room = new ChatRoom();
        room.setDoctor(doctor);
        room.setPatient(patient);
        entityManager.persist(room);
        entityManager.flush();

        Optional<ChatRoom> foundRoom = chatRoomRepository.findByDoctorAndPatient(doctor, patient);
        assertTrue(foundRoom.isPresent());
        assertEquals(doctor.getId(), foundRoom.get().getDoctor().getId());
        assertEquals(patient.getId(), foundRoom.get().getPatient().getId());
    }
}