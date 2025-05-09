package id.ac.ui.cs.advprog.pandacare.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;

class ChatRoomTest {

    @Test
    void testChatRoomBuilder() {
        User doctor = User.builder().id(1L).name("Dr. Smith").build();
        User patient = User.builder().id(2L).name("John Doe").build();
        
        ChatRoom chatRoom = ChatRoom.builder()
                .id(1L)
                .doctor(doctor)
                .patient(patient)
                .messages(new ArrayList<>())
                .build();
        
        assertThat(chatRoom.getId()).isEqualTo(1L);
        assertThat(chatRoom.getDoctor()).isEqualTo(doctor);
        assertThat(chatRoom.getPatient()).isEqualTo(patient);
        assertThat(chatRoom.getMessages()).isEmpty();
    }
    
    @Test
    void testChatRoomSettersGetters() {
        ChatRoom chatRoom = new ChatRoom();
        User doctor = User.builder().id(1L).name("Dr. Smith").build();
        User patient = User.builder().id(2L).name("John Doe").build();
        
        chatRoom.setId(1L);
        chatRoom.setDoctor(doctor);
        chatRoom.setPatient(patient);
        chatRoom.setMessages(new ArrayList<>());
        
        assertThat(chatRoom.getId()).isEqualTo(1L);
        assertThat(chatRoom.getDoctor().getName()).isEqualTo("Dr. Smith");
        assertThat(chatRoom.getPatient().getName()).isEqualTo("John Doe");
    }
    
    @Test
    void testChatRoomEquality() {
        ChatRoom room1 = ChatRoom.builder().id(1L).build();
        ChatRoom room2 = ChatRoom.builder().id(1L).build();
        ChatRoom room3 = ChatRoom.builder().id(2L).build();
        
        assertThat(room1).isEqualTo(room2);
        assertThat(room1).isNotEqualTo(room3);
    }
}