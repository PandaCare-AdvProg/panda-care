package id.ac.ui.cs.advprog.pandacare.model;

import id.ac.ui.cs.advprog.pandacare.Auth.User;
import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.observer.ChatObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatMessageObserverTest {

    private ChatMessage chatMessage;
    private User sender;
    private User receiver;
    private ChatObserver mockObserver;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(1L);
        sender.setRole(Role.DOCTOR);

        receiver = new User();
        receiver.setId(2L);
        receiver.setRole(Role.PATIENT);

        chatMessage = new ChatMessage();
        chatMessage.setId(1L);
        chatMessage.setSender(sender);
        chatMessage.setReceiver(receiver);
        chatMessage.setSenderRole(Role.DOCTOR);
        chatMessage.setContent("Initial message");
        chatMessage.setCreatedAt(LocalDateTime.now());

        mockObserver = Mockito.mock(ChatObserver.class);
        chatMessage.addObserver(mockObserver);
    }

    @Test
    void testAddObserver() {
        ChatObserver newObserver = Mockito.mock(ChatObserver.class);
        chatMessage.addObserver(newObserver);

        chatMessage.notifyObservers("Test event");
        verify(newObserver, times(1)).update(any(ChatMessage.class), anyString());
    }

    @Test
    void testRemoveObserver() {
        chatMessage.removeObserver(mockObserver);

        chatMessage.notifyObservers("Test event");
        verify(mockObserver, never()).update(any(ChatMessage.class), anyString());
    }

    @Test
    void testNotifyObserversOnContentUpdate() {

        chatMessage.setContent("Updated message");

        ArgumentCaptor<ChatMessage> messageCaptor = ArgumentCaptor.forClass(ChatMessage.class);
        ArgumentCaptor<String> eventCaptor = ArgumentCaptor.forClass(String.class);

        verify(mockObserver, times(1)).update(messageCaptor.capture(), eventCaptor.capture());

        assertEquals(chatMessage, messageCaptor.getValue());
        assertEquals("Content updated", eventCaptor.getValue());
        assertTrue(chatMessage.isEdited());
    }

    @Test
    void testNotifyObserversOnDelete() {
        chatMessage.markAsDeleted();

        ArgumentCaptor<String> eventCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockObserver, times(1)).update(eq(chatMessage), eventCaptor.capture());

        assertEquals("Message deleted", eventCaptor.getValue());
        assertTrue(chatMessage.isDeleted());
    }

    @Test
    void testNotifyObserversOnUpdate() {
        chatMessage.onUpdate();

        verify(mockObserver, times(1)).update(eq(chatMessage), eq("Message updated"));
    }

    @Test
    void testNoNotificationForNewMessageContentChange() {
        ChatMessage newMessage = new ChatMessage();
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        newMessage.addObserver(mockObserver);

        newMessage.setContent("New content");

        verify(mockObserver, never()).update(any(), any());
        assertFalse(newMessage.isEdited()); // Shouldn't be marked as edited either
    }

    @Test
    void testMultipleObservers() {
        ChatObserver secondObserver = Mockito.mock(ChatObserver.class);
        chatMessage.addObserver(secondObserver);

        chatMessage.setContent("Multi-observer test");

        verify(mockObserver, times(1)).update(eq(chatMessage), eq("Content updated"));
        verify(secondObserver, times(1)).update(eq(chatMessage), eq("Content updated"));
    }

    @Test
    void testObserverRemovalDuringNotification() {
        ChatObserver selfRemovingObserver = new ChatObserver() {
            @Override
            public void update(ChatMessage message, String event) {
                message.removeObserver(this);
            }
        };
        chatMessage.addObserver(selfRemovingObserver);

        chatMessage.setContent("Test self-removing observer");

        assertDoesNotThrow(() -> chatMessage.notifyObservers("Test event"));
    }
}