package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import id.ac.ui.cs.advprog.pandacare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByDoctor(User doctor);
    List<ChatRoom> findByPatient(User patient);
    Optional<ChatRoom> findByDoctorAndPatient(User doctor, User patient);
    
    @Query("SELECT r FROM ChatRoom r WHERE r.doctor.id = :userId OR r.patient.id = :userId")
    List<ChatRoom> findByUserId(Long userId);
}
