package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.dto.ChatMessageDTO;
import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;
import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import id.ac.ui.cs.advprog.pandacare.model.User;
import id.ac.ui.cs.advprog.pandacare.observer.ChatObserver;
import id.ac.ui.cs.advprog.pandacare.repository.ChatMessageRepository;
import id.ac.ui.cs.advprog.pandacare.repository.ChatRoomRepository;
import id.ac.ui.cs.advprog.pandacare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final List<ChatObserver> chatObservers;

    @Override
    @Transactional
    public ChatRoom createRoom(Long doctorId, Long patientId) {
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        
        return chatRoomRepository.findByDoctorAndPatient(doctor, patient)
                .orElseGet(() -> {
                    ChatRoom newRoom = ChatRoom.builder()
                            .doctor(doctor)
                            .patient(patient)
                            .messages(new ArrayList<>())
                            .build();
                    return chatRoomRepository.save(newRoom);
                });
    }

    @Override
    public ChatRoom getRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
    }

    @Override
    public List<ChatRoom> getRoomsByUserId(Long userId) {
        return chatRoomRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public ChatMessage sendMessage(Long roomId, Long senderId, String content) {
        ChatRoom room = getRoomById(roomId);
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        
        // Determine receiver based on the sender and room
        User receiver;
        if (sender.getId().equals(room.getDoctor().getId())) {
            receiver = room.getPatient();
        } else if (sender.getId().equals(room.getPatient().getId())) {
            receiver = room.getDoctor();
        } else {
            throw new IllegalArgumentException("Sender is not part of this chat room");
        }
        
        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .room(room)
                .senderRole(sender.getRole())
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        
        // Register observers
        for (ChatObserver observer : chatObservers) {
            message.addObserver(observer);
        }
        
        ChatMessage savedMessage = chatMessageRepository.save(message);
        message.notifyObservers("Message sent");
        
        return savedMessage;
    }

    @Override
    @Transactional
    public ChatMessage editMessage(Long messageId, Long senderId, String newContent) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        
        if (!message.getSender().getId().equals(senderId)) {
            throw new IllegalArgumentException("You can only edit your own messages");
        }
        
        // Since setContent has observer notification logic, we should register observers first
        for (ChatObserver observer : chatObservers) {
            message.addObserver(observer);
        }
        
        message.setContent(newContent);
        return chatMessageRepository.save(message);
    }

    @Override
    @Transactional
    public ChatMessage deleteMessage(Long messageId, Long senderId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        
        if (!message.getSender().getId().equals(senderId)) {
            throw new IllegalArgumentException("You can only delete your own messages");
        }
        
        // Register observers
        for (ChatObserver observer : chatObservers) {
            message.addObserver(observer);
        }
        
        message.setDeleted(true);
        message.onUpdate();
        return chatMessageRepository.save(message);
    }

    @Override
    public List<ChatMessage> getRoomMessages(Long roomId) {
        ChatRoom room = getRoomById(roomId);
        return chatMessageRepository.findByRoomOrderByCreatedAtAsc(room);
    }

    @Override
    public ChatMessageDTO mapToDTO(ChatMessage message) {
        return ChatMessageDTO.builder()
                .id(message.getId())
                .roomId(message.getRoom().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .senderRole(message.getSenderRole())
                .content(message.isDeleted() ? "[Message deleted]" : message.getContent())
                .edited(message.isEdited())
                .deleted(message.isDeleted())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }

    @Override
    public List<ChatMessageDTO> mapToDTOList(List<ChatMessage> messages) {
        return messages.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}