package id.ac.ui.cs.advprog.pandacare.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;
import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import id.ac.ui.cs.advprog.pandacare.repository.ChatMessageRepository;
import id.ac.ui.cs.advprog.pandacare.repository.ChatRoomRepository;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class ChatMediatorImpl implements ChatMediator {
    private final ChatMessageRepository messageRepository;
    private final ChatRoomRepository roomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMediatorImpl(ChatMessageRepository messageRepository, ChatRoomRepository roomRepository, SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public ChatMessage sendMessage(String roomId, String senderId, String receiverId, String content) {
        ChatRoom room;
        if (roomId != null) {
            room = roomRepository.findByRoomId(roomId);
        } else {
            room = roomRepository.findByPacilianIdAndCaregiverId(senderId, receiverId);
            if (room == null) {
                room = roomRepository.findByPacilianIdAndCaregiverId(receiverId, senderId);
            }
            if (room == null) {
                String newRoomId = UUID.randomUUID().toString();
                room = new ChatRoom(newRoomId, senderId, receiverId);
                roomRepository.save(room);
            }
        }
        
        ChatMessage message = new ChatMessage();
        message.setSender(senderId);
        message.setReceiver(receiverId); 
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now()); 
        message.setChatRoom(room);
        
        message = messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/chat/" + room.getRoomId(), message);
        
        return message;
    }

    public void editMessage(ChatMessage message, String roomIdString) {
        messagingTemplate.convertAndSend("/topic/chat/" + roomIdString + "/edit", message);
    }

    public void deleteMessage(String roomId, String messageId) {
        messagingTemplate.convertAndSend("/topic/chat/" + roomId + "/delete", messageId);
    }
}