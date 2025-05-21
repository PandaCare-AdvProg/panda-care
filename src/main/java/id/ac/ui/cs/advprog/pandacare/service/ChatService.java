package id.ac.ui.cs.advprog.pandacare.service;

import java.util.List;
import java.util.Optional;

import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;
import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;

public interface ChatService {
    ChatMessage sendMessage(String roomId, String senderId, String receiverId, String content);
    List<ChatMessage> getMessagesByRoomId(String roomId);
    ChatRoom getChatRoomByPacilianAndCaregiver(String pacilianId, String caregiverId);
    List<ChatRoom> getChatRoomsByPacilianId(String pacilianId);
    List<ChatRoom> getChatRoomsByCaregiverId(String caregiverId);
    Optional<ChatMessage> editMessage(String roomId, String messageId, String content);
    void deleteMessage(String roomId, String messageId);
} 