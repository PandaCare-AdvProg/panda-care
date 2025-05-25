package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.dto.DeleteChatRequest;
import id.ac.ui.cs.advprog.pandacare.dto.EditChatRequest;
import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;
import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import id.ac.ui.cs.advprog.pandacare.service.ChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketControllerTest {

    @Mock
    private ChatService chatService;

    @Mock
    private SimpMessageHeaderAccessor headerAccessor;

    @InjectMocks
    private WebSocketController webSocketController;

    @Test
    void testSendMessage() {
        // Arrange
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("sender", "user1");
        messageMap.put("receiver", "user2");
        messageMap.put("content", "Hello");

        ChatMessage expectedMessage = new ChatMessage();
        when(chatService.sendMessage("room-123", "user1", "user2", "Hello"))
            .thenReturn(expectedMessage);

        // Act
        webSocketController.sendMessage("room-123", messageMap, headerAccessor);

        // Assert
        verify(chatService).sendMessage("room-123", "user1", "user2", "Hello");
    }

    @Test
    void testSubscribeToRoom() {
        // Arrange
        List<ChatMessage> expectedMessages = Collections.singletonList(new ChatMessage());
        when(chatService.getMessagesByRoomId("room-123")).thenReturn(expectedMessages);

        // Act
        List<ChatMessage> result = webSocketController.subscribeToRoom("room-123");

        // Assert
        assertEquals(expectedMessages, result);
        verify(chatService).getMessagesByRoomId("room-123");
    }

    @Test
    void testCreateRoom() {
        // Arrange
        ChatRoom expectedRoom = new ChatRoom();
        when(chatService.getChatRoomByPacilianAndCaregiver("user1", "user2"))
            .thenReturn(expectedRoom);

        // Act
        ChatRoom result = webSocketController.createRoom("user1", "user2");

        // Assert
        assertEquals(expectedRoom, result);
        verify(chatService).getChatRoomByPacilianAndCaregiver("user1", "user2");
    }

    @Test
    void testEditMessage() {
        // Arrange
        EditChatRequest request = new EditChatRequest();
        request.setMessageId("msg-123");
        request.setContent("Updated content");

        ChatMessage expectedMessage = new ChatMessage();
        when(chatService.editMessage("room-123", "msg-123", "Updated content"))
            .thenReturn(Optional.of(expectedMessage));

        // Act
        Optional<ChatMessage> result = webSocketController.editMessage("room-123", request);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedMessage, result.get());
        verify(chatService).editMessage("room-123", "msg-123", "Updated content");
    }

    @Test
    void testDeleteMessage() {
        // Arrange
        DeleteChatRequest request = new DeleteChatRequest();
        request.setMessageId("msg-123");

        // Act
        webSocketController.delete("room-123", request);

        // Assert
        verify(chatService).deleteMessage("room-123", "msg-123");
    }
}