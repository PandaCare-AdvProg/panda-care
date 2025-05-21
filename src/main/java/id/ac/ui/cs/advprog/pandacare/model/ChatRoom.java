package id.ac.ui.cs.advprog.pandacare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    private String roomId;
    
    private String pacilianId;

    private String caregiverId;
    
    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.EAGER)
    private List<ChatMessage> messages = new ArrayList<>();

    public ChatRoom(String roomId, String pacilianId, String caregiverId) {
        this.roomId = roomId;
        this.pacilianId = pacilianId;
        this.caregiverId = caregiverId;
        this.messages = new ArrayList<>();
    }
    
    public void addNewMessage(ChatMessage message) {
        if (messages == null) {messages = new ArrayList<>(); }
        messages.add(message);
    }
} 