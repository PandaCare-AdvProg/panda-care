package id.ac.ui.cs.advprog.pandacare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    ChatRoom findByPacilianIdAndCaregiverId(String pacilianId, String caregiverId);
    List<ChatRoom> findByPacilianId(String pacilianId);
    List<ChatRoom> findByCaregiverId(String caregiverId);
    ChatRoom findByRoomId(String roomId);
}