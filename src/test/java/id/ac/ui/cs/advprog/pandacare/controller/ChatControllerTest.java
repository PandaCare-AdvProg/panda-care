package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.dto.ApiResponse;
import id.ac.ui.cs.advprog.pandacare.dto.ChatMessageDTO;
import id.ac.ui.cs.advprog.pandacare.dto.ChatRequest;
import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;
import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import id.ac.ui.cs.advprog.pandacare.model.User;
import id.ac.ui.cs.advprog.pandacare.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ChatController chatController;

    private User doctor;
    private User patient;
    private ChatRoom chatRoom;
    private ChatMessage chatMessage;
    private ChatMessageDTO chatMessageDTO;
    private Authentication authentication;
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup mock security context
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("1"); // Current user ID
        
        // Setup test data
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
                .build();
                
        chatMessage = ChatMessage.builder()
                .id(1L)
                .sender(doctor)
                .receiver(patient)
                .room(chatRoom)
                .content("Test message")
                .createdAt(LocalDateTime.now())
                .build();
                
        chatMessageDTO = ChatMessageDTO.builder()
                .id(1L)
                .roomId(1L)
                .senderId(1L)
                .senderName("Dr. Smith")
                .senderRole(Role.DOCTOR)
                .content("Test message")
                .edited(false)
                .deleted(false)
                .build();
    }

    @Test
    void testCreateRoom() {
        when(chatService.createRoom(1L, 2L)).thenReturn(chatRoom);

        ResponseEntity<ApiResponse<ChatRoom>> response = chatController.createRoom(1L, 2L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Chat room created successfully", response.getBody().getMessage());
        assertEquals(chatRoom, response.getBody().getData());
        verify(chatService).createRoom(1L, 2L);
    }

    @Test
    void testGetUserRooms() {
        List<ChatRoom> rooms = Arrays.asList(chatRoom);
        when(chatService.getRoomsByUserId(1L)).thenReturn(rooms);

        ResponseEntity<ApiResponse<List<ChatRoom>>> response = chatController.getUserRooms();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Chat rooms retrieved successfully", response.getBody().getMessage());
        assertEquals(rooms, response.getBody().getData());
        verify(chatService).getRoomsByUserId(1L);
    }

    @Test
    void testGetRoomById() {
        when(chatService.getRoomById(1L)).thenReturn(chatRoom);

        ResponseEntity<ApiResponse<ChatRoom>> response = chatController.getRoomById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Chat room retrieved successfully", response.getBody().getMessage());
        assertEquals(chatRoom, response.getBody().getData());
        verify(chatService).getRoomById(1L);
    }

    @Test
    void testGetRoomMessages() {
        List<ChatMessage> messages = Arrays.asList(chatMessage);
        List<ChatMessageDTO> messageDTOs = Arrays.asList(chatMessageDTO);
        
        when(chatService.getRoomMessages(1L)).thenReturn(messages);
        when(chatService.mapToDTOList(messages)).thenReturn(messageDTOs);

        ResponseEntity<ApiResponse<List<ChatMessageDTO>>> response = chatController.getRoomMessages(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Messages retrieved successfully", response.getBody().getMessage());
        assertEquals(messageDTOs, response.getBody().getData());
        verify(chatService).getRoomMessages(1L);
        verify(chatService).mapToDTOList(messages);
    }

    @Test
    void testSendMessage() {
        ChatRequest request = new ChatRequest();
        request.setRoomId(1L);
        request.setContent("Test message");
        
        when(chatService.sendMessage(eq(1L), eq(1L), anyString())).thenReturn(chatMessage);
        when(chatService.mapToDTO(any(ChatMessage.class))).thenReturn(chatMessageDTO);

        ResponseEntity<ApiResponse<ChatMessageDTO>> response = chatController.sendMessage(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Message sent successfully", response.getBody().getMessage());
        assertEquals(chatMessageDTO, response.getBody().getData());
        verify(chatService).sendMessage(eq(1L), eq(1L), anyString());
        verify(chatService).mapToDTO(chatMessage);
    }

    @Test
    void testEditMessage() {
        ChatRequest request = new ChatRequest();
        request.setContent("Updated message");
        
        when(chatService.editMessage(eq(1L), eq(1L), anyString())).thenReturn(chatMessage);
        when(chatService.mapToDTO(any(ChatMessage.class))).thenReturn(chatMessageDTO);

        ResponseEntity<ApiResponse<ChatMessageDTO>> response = chatController.editMessage(1L, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Message edited successfully", response.getBody().getMessage());
        assertEquals(chatMessageDTO, response.getBody().getData());
        verify(chatService).editMessage(eq(1L), eq(1L), anyString());
        verify(chatService).mapToDTO(chatMessage);
    }

    @Test
    void testDeleteMessage() {
        when(chatService.deleteMessage(eq(1L), eq(1L))).thenReturn(chatMessage);
        when(chatService.mapToDTO(any(ChatMessage.class))).thenReturn(chatMessageDTO);

        ResponseEntity<ApiResponse<ChatMessageDTO>> response = chatController.deleteMessage(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Message deleted successfully", response.getBody().getMessage());
        assertEquals(chatMessageDTO, response.getBody().getData());
        verify(chatService).deleteMessage(1L, 1L);
        verify(chatService).mapToDTO(chatMessage);
    }
}