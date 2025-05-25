package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;
import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import id.ac.ui.cs.advprog.pandacare.repository.ChatMessageRepository;
import id.ac.ui.cs.advprog.pandacare.repository.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @Mock
    private ChatMessageRepository messageRepository;

    @Mock
    private ChatRoomRepository roomRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private ChatMediator chatMediator;

    @InjectMocks
    private ChatServiceImpl chatService;

    private ChatRoom testRoom;
    private ChatMessage testMessage;

    @BeforeEach
    void setUp() {
        testRoom = new ChatRoom("room-1", "pac-1", "care-1");
        testMessage = new ChatMessage("sender-1", "receiver-1", "Hello", LocalDateTime.now(), testRoom);
        testMessage.setId("msg-1");
    }

    @Test
    void testSendMessage() {
        when(chatMediator.sendMessage("room-1", "sender-1", "receiver-1", "Hello"))
            .thenReturn(testMessage);

        ChatMessage result = chatService.sendMessage("room-1", "sender-1", "receiver-1", "Hello");

        assertNotNull(result);
        assertEquals("Hello", result.getContent());
        verify(chatMediator).sendMessage("room-1", "sender-1", "receiver-1", "Hello");
    }

    @Test
    void testGetMessagesByRoomId() {
        ChatMessage message1 = new ChatMessage("sender-1", "receiver-1", "Message 1", LocalDateTime.now().minusHours(1), testRoom);
        ChatMessage message2 = new ChatMessage("sender-1", "receiver-1", "Message 2", LocalDateTime.now(), testRoom);
        
        when(messageRepository.findByChatRoomRoomId("room-1"))
            .thenReturn(Arrays.asList(message2, message1));

        List<ChatMessage> messages = chatService.getMessagesByRoomId("room-1");

        assertEquals(2, messages.size());
        assertEquals("Message 1", messages.get(0).getContent()); // Should be first after sorting
        assertEquals("Message 2", messages.get(1).getContent());
        verify(messageRepository).findByChatRoomRoomId("room-1");
    }

    @Test
    void testGetChatRoomByPacilianAndCaregiver_ExistingRoom() {
        when(roomRepository.findByPacilianIdAndCaregiverId("pac-1", "care-1"))
            .thenReturn(testRoom);

        ChatRoom result = chatService.getChatRoomByPacilianAndCaregiver("pac-1", "care-1");

        assertNotNull(result);
        assertEquals("room-1", result.getRoomId());
        verify(roomRepository).findByPacilianIdAndCaregiverId("pac-1", "care-1");
        verify(roomRepository, never()).save(any());
    }

    @Test
    void testGetChatRoomByPacilianAndCaregiver_NewRoom() {
        when(roomRepository.findByPacilianIdAndCaregiverId("pac-2", "care-2"))
            .thenReturn(null);
        when(roomRepository.save(any(ChatRoom.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        ChatRoom result = chatService.getChatRoomByPacilianAndCaregiver("pac-2", "care-2");

        assertNotNull(result);
        assertEquals("pac-2", result.getPacilianId());
        assertEquals("care-2", result.getCaregiverId());
        verify(roomRepository).findByPacilianIdAndCaregiverId("pac-2", "care-2");
        verify(roomRepository).save(any(ChatRoom.class));
    }

    @Test
    void testGetChatRoomsByPacilianId() {
        ChatRoom room1 = new ChatRoom("room-1", "pac-1", "care-1");
        ChatRoom room2 = new ChatRoom("room-2", "pac-1", "care-2");
        
        when(roomRepository.findByPacilianId("pac-1"))
            .thenReturn(Arrays.asList(room1, room2));

        List<ChatRoom> rooms = chatService.getChatRoomsByPacilianId("pac-1");

        assertEquals(2, rooms.size());
        assertTrue(rooms.stream().allMatch(r -> r.getPacilianId().equals("pac-1")));
        verify(roomRepository).findByPacilianId("pac-1");
    }

    @Test
    void testGetChatRoomsByCaregiverId() {
        ChatRoom room1 = new ChatRoom("room-1", "pac-1", "care-1");
        ChatRoom room2 = new ChatRoom("room-3", "pac-2", "care-1");
        
        when(roomRepository.findByCaregiverId("care-1"))
            .thenReturn(Arrays.asList(room1, room2));

        List<ChatRoom> rooms = chatService.getChatRoomsByCaregiverId("care-1");

        assertEquals(2, rooms.size());
        assertTrue(rooms.stream().allMatch(r -> r.getCaregiverId().equals("care-1")));
        verify(roomRepository).findByCaregiverId("care-1");
    }

    @Test
    void testEditMessage_Success() {
        ChatMessage editedMessage = new ChatMessage("sender-1", "receiver-1", "Edited", LocalDateTime.now(), testRoom);
        editedMessage.setId("msg-1");
        editedMessage.setEdited(true);
        
        when(messageRepository.findById("msg-1"))
            .thenReturn(Optional.of(testMessage));
        when(messageRepository.save(any(ChatMessage.class)))
            .thenReturn(editedMessage);

        Optional<ChatMessage> result = chatService.editMessage("room-1", "msg-1", "Edited");

        assertTrue(result.isPresent());
        assertEquals("Edited", result.get().getContent());
        assertTrue(result.get().isEdited());
        verify(messageRepository).findById("msg-1");
        verify(messageRepository).save(any(ChatMessage.class));
        verify(chatMediator).editMessage(any(ChatMessage.class), eq("room-1"));
    }

    @Test
    void testEditMessage_NotFound() {
        when(messageRepository.findById("nonexistent"))
            .thenReturn(Optional.empty());

        Optional<ChatMessage> result = chatService.editMessage("room-1", "nonexistent", "Edited");

        assertFalse(result.isPresent());
        verify(messageRepository).findById("nonexistent");
        verify(messageRepository, never()).save(any());
        verify(chatMediator, never()).editMessage(any(), any());
    }

    @Test
    void testDeleteMessage() {
        doNothing().when(messageRepository).deleteById("msg-1");

        chatService.deleteMessage("room-1", "msg-1");

        verify(messageRepository).deleteById("msg-1");
        verify(chatMediator).deleteMessage("room-1", "msg-1");
    }
}