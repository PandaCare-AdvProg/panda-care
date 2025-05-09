package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.dto.ChatMessageDTO;
import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;
import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import id.ac.ui.cs.advprog.pandacare.model.User;
import id.ac.ui.cs.advprog.pandacare.observer.ChatObserver;
import id.ac.ui.cs.advprog.pandacare.repository.ChatMessageRepository;
import id.ac.ui.cs.advprog.pandacare.repository.ChatRoomRepository;
import id.ac.ui.cs.advprog.pandacare.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;
    
    @Mock
    private ChatMessageRepository chatMessageRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private List<ChatObserver> chatObservers;
    
    @InjectMocks
    private ChatServiceImpl chatService;
    
    private User doctor;
    private User patient;
    private ChatRoom chatRoom;
    private ChatMessage chatMessage;
    
    @BeforeEach
    void setUp() {
        doctor = User.builder()
                .id(1L)
                .name("Dr. Smith")
                .role(Role.DOCTOR)
                .build();
                
        patient = User.builder()
                .id(2L)
                .name("John Doe")
                .role(Role.PATIENT)
                .build();
                
        chatRoom = ChatRoom.builder()
                .id(1L)
                .doctor(doctor)
                .patient(patient)
                .messages(new ArrayList<>())
                .build();
                
        chatMessage = ChatMessage.builder()
                .id(1L)
                .sender(doctor)
                .receiver(patient)
                .room(chatRoom)
                .senderRole(Role.DOCTOR)
                .content("Test message")
                .createdAt(LocalDateTime.now())
                .build();
    }
    
    @Test
    void createRoom_WhenRoomExists_ReturnExistingRoom() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(userRepository.findById(2L)).thenReturn(Optional.of(patient));
        when(chatRoomRepository.findByDoctorAndPatient(doctor, patient))
            .thenReturn(Optional.of(chatRoom));
        
        ChatRoom result = chatService.createRoom(1L, 2L);
        
        assertThat(result).isEqualTo(chatRoom);
        verify(chatRoomRepository).findByDoctorAndPatient(doctor, patient);
        verify(chatRoomRepository, never()).save(any());
    }
    
    @Test
    void createRoom_WhenRoomDoesNotExist_CreateAndReturnNewRoom() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(userRepository.findById(2L)).thenReturn(Optional.of(patient));
        when(chatRoomRepository.findByDoctorAndPatient(doctor, patient))
            .thenReturn(Optional.empty());
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);
        
        ChatRoom result = chatService.createRoom(1L, 2L);
        
        assertThat(result).isEqualTo(chatRoom);
        verify(chatRoomRepository).findByDoctorAndPatient(doctor, patient);
        verify(chatRoomRepository).save(any(ChatRoom.class));
    }
    
    @Test
    void getRoomById_WhenRoomExists_ReturnRoom() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        
        ChatRoom result = chatService.getRoomById(1L);
        
        assertThat(result).isEqualTo(chatRoom);
    }
    
    @Test
    void getRoomById_WhenRoomDoesNotExist_ThrowException() {
        when(chatRoomRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(IllegalArgumentException.class, () -> chatService.getRoomById(999L));
    }
    
    @Test
    void getRoomsByUserId_ReturnRooms() {
        List<ChatRoom> rooms = Arrays.asList(chatRoom);
        when(chatRoomRepository.findByUserId(1L)).thenReturn(rooms);
        
        List<ChatRoom> result = chatService.getRoomsByUserId(1L);
        
        assertThat(result).isEqualTo(rooms);
    }
    
    @Test
    void sendMessage_CreateAndReturnMessage() {
        ChatObserver observer = mock(ChatObserver.class);
        when(chatObservers.iterator()).thenReturn(List.of(observer).iterator());
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        when(userRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(i -> i.getArgument(0));
        
        ChatMessage result = chatService.sendMessage(1L, 1L, "Test message");
        
        assertThat(result.getContent()).isEqualTo("Test message");
        assertThat(result.getSender()).isEqualTo(doctor);
        assertThat(result.getReceiver()).isEqualTo(patient);
        assertThat(result.getRoom()).isEqualTo(chatRoom);
        verify(chatMessageRepository).save(any(ChatMessage.class));
    }
    
    @Test
    void mapToDTO_ConvertChatMessageToDTO() {
        ChatMessageDTO dto = chatService.mapToDTO(chatMessage);
        
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getRoomId()).isEqualTo(1L);
        assertThat(dto.getSenderId()).isEqualTo(1L);
        assertThat(dto.getSenderName()).isEqualTo("Dr. Smith");
        assertThat(dto.getSenderRole()).isEqualTo(Role.DOCTOR);
        assertThat(dto.getContent()).isEqualTo("Test message");
        assertThat(dto.isEdited()).isFalse();
        assertThat(dto.isDeleted()).isFalse();
    }
}