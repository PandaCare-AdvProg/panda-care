package id.ac.ui.cs.advprog.pandacare.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;
import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import id.ac.ui.cs.advprog.pandacare.repository.ChatMessageRepository;
import id.ac.ui.cs.advprog.pandacare.repository.ChatRoomRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {
    
    private final ChatMessageRepository messageRepository;
    private final ChatRoomRepository roomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMediator chatMediator;
    
    public ChatServiceImpl(
            ChatMessageRepository messageRepository, 
            ChatRoomRepository roomRepository, 
            SimpMessagingTemplate messagingTemplate,
            ChatMediator chatMediator) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.messagingTemplate = messagingTemplate;
        this.chatMediator = chatMediator;
    }
    
    @Override
    public ChatMessage sendMessage(String roomId, String senderId, String receiverId, String content) {
        return chatMediator.sendMessage(roomId, senderId, receiverId, content);
    }

    @Override
    public List<ChatMessage> getMessagesByRoomId(String roomId) {
        List<ChatMessage> messages = messageRepository.findByChatRoomRoomId(roomId)
            .stream()
            .sorted(Comparator.comparing(ChatMessage::getTimestamp))
            .collect(Collectors.toList());
        System.out.println("Found " + messages.size() + " messages for room " + roomId);
        return messages;
    }
    
    @Override
    public ChatRoom getChatRoomByPacilianAndCaregiver(String pacilianId, String caregiverId) {
        ChatRoom room = roomRepository.findByPacilianIdAndCaregiverId(pacilianId, caregiverId);
        ChatRoom roomtoo = roomRepository.findByPacilianIdAndCaregiverId(caregiverId, pacilianId);

        if (roomtoo != null && room == null) {
            room = roomtoo;
        }   

        if (room == null) {
            String roomId = UUID.randomUUID().toString();
            room = new ChatRoom(roomId, pacilianId, caregiverId);
            roomRepository.save(room);
        }
        
        return room;
    }
    
    @Override
    public List<ChatRoom> getChatRoomsByPacilianId(String pacilianId) {
        return roomRepository.findByPacilianId(pacilianId);
    }
    
    @Override
    public List<ChatRoom> getChatRoomsByCaregiverId(String caregiverId) {
        return roomRepository.findByCaregiverId(caregiverId);
    }

    @Override
    public Optional<ChatMessage> editMessage(String roomId, String messageId, String content) {
        return messageRepository.findById(messageId)
            .map(msg -> {
                msg.setContent(content);
                msg.setEdited(true);
                ChatMessage saved = messageRepository.save(msg);
                chatMediator.editMessage(saved, roomId);
                return saved;
            });
    }

    @Override
    public void deleteMessage(String roomId, String messageId) {
        messageRepository.deleteById(messageId);
        chatMediator.deleteMessage(roomId, messageId);
    }

} 