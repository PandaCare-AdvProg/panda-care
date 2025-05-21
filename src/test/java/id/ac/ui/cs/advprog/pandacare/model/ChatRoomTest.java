package id.ac.ui.cs.advprog.pandacare.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class ChatRoomTest {

    private ChatRoom chatRoom;
    private final String testRoomId = "room-123";
    private final String testPacilianId = "pacilian-456";
    private final String testCaregiverId = "caregiver-789";

    @BeforeEach
    void setUp() {
        chatRoom = new ChatRoom(testRoomId, testPacilianId, testCaregiverId);
    }

    @Test
    void testChatRoomCreation() {
        assertNotNull(chatRoom);
        assertEquals(testRoomId, chatRoom.getRoomId());
        assertEquals(testPacilianId, chatRoom.getPacilianId());
        assertEquals(testCaregiverId, chatRoom.getCaregiverId());
        assertNotNull(chatRoom.getMessages());
        assertTrue(chatRoom.getMessages().isEmpty());
    }

    @Test
    void testDefaultConstructor() {
        ChatRoom defaultRoom = new ChatRoom();
        assertNull(defaultRoom.getRoomId());
        assertNull(defaultRoom.getPacilianId());
        assertNull(defaultRoom.getCaregiverId());
        assertNotNull(defaultRoom.getMessages());
        assertTrue(defaultRoom.getMessages().isEmpty());
    }

    @Test
    void testSetterMethods() {
        chatRoom.setRoomId("new-room-id");
        assertEquals("new-room-id", chatRoom.getRoomId());

        chatRoom.setPacilianId("new-pacilian-id");
        assertEquals("new-pacilian-id", chatRoom.getPacilianId());

        chatRoom.setCaregiverId("new-caregiver-id");
        assertEquals("new-caregiver-id", chatRoom.getCaregiverId());

        List<ChatMessage> newMessages = new ArrayList<>();
        ChatMessage message = new ChatMessage();
        message.setContent("Test message");
        newMessages.add(message);
        
        chatRoom.setMessages(newMessages);
        assertEquals(1, chatRoom.getMessages().size());
        assertEquals("Test message", chatRoom.getMessages().get(0).getContent());
    }

    @Test
    void testAddNewMessage() {
        ChatMessage message1 = new ChatMessage();
        message1.setContent("First message");
        
        ChatMessage message2 = new ChatMessage();
        message2.setContent("Second message");

        chatRoom.addNewMessage(message1);
        chatRoom.addNewMessage(message2);

        assertEquals(2, chatRoom.getMessages().size());
        assertEquals("First message", chatRoom.getMessages().get(0).getContent());
        assertEquals("Second message", chatRoom.getMessages().get(1).getContent());
    }

    @Test
    void testAddNewMessageWithNullMessagesList() {
        chatRoom.setMessages(null); // Simulate null messages list
        ChatMessage message = new ChatMessage();
        message.setContent("Test message");
        
        chatRoom.addNewMessage(message);
        
        assertNotNull(chatRoom.getMessages());
        assertEquals(1, chatRoom.getMessages().size());
        assertEquals("Test message", chatRoom.getMessages().get(0).getContent());
    }

    @Test
    void testToString() {
        String roomString = chatRoom.toString();
        assertNotNull(roomString);
        assertTrue(roomString.contains("ChatRoom"));
        assertTrue(roomString.contains(testRoomId));
    }

    @Test
    void testEqualsAndHashCode() {
        ChatRoom room1 = new ChatRoom("room-1", "pac-1", "care-1");
        ChatRoom room2 = new ChatRoom("room-1", "pac-1", "care-1");
        ChatRoom differentRoom = new ChatRoom("room-2", "pac-2", "care-2");

        assertEquals(room1, room2);
        assertNotEquals(room1, differentRoom);
        assertEquals(room1.hashCode(), room2.hashCode());
        assertNotEquals(room1.hashCode(), differentRoom.hashCode());
    }

    @Test
    void testMessageBidirectionalRelationship() {
        ChatMessage message = new ChatMessage();
        message.setContent("Test message");
        message.setChatRoom(chatRoom);
        
        chatRoom.addNewMessage(message);
        
        assertEquals(1, chatRoom.getMessages().size());
        assertEquals(chatRoom, message.getChatRoom());
    }
}