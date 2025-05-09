package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.dto.ChatMessageDTO;
import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;
import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import id.ac.ui.cs.advprog.pandacare.model.User;

import java.util.List;

public interface ChatService {
    // Room management
    ChatRoom createRoom(Long doctorId, Long patientId);
    ChatRoom getRoomById(Long roomId);
    List<ChatRoom> getRoomsByUserId(Long userId);
    
    // Message operations
    ChatMessage sendMessage(Long roomId, Long senderId, String content);
    ChatMessage editMessage(Long messageId, Long senderId, String newContent);
    ChatMessage deleteMessage(Long messageId, Long senderId);
    List<ChatMessage> getRoomMessages(Long roomId);
    
    // DTO transformation
    ChatMessageDTO mapToDTO(ChatMessage message);
    List<ChatMessageDTO> mapToDTOList(List<ChatMessage> messages);
}