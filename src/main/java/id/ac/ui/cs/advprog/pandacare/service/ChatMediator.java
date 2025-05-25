package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;

public interface ChatMediator {
    ChatMessage sendMessage(String roomId, String senderId, String receiverId, String content);
    void editMessage(ChatMessage message, String roomIdString);
    void deleteMessage(String roomId, String messageId);
} 