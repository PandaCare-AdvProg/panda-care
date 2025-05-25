package id.ac.ui.cs.advprog.pandacare.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import id.ac.ui.cs.advprog.pandacare.dto.DeleteChatRequest;
import id.ac.ui.cs.advprog.pandacare.dto.EditChatRequest;
import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;
import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import id.ac.ui.cs.advprog.pandacare.service.ChatService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class WebSocketController {
    
    private final ChatService chatService;
    
    public WebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(
            @DestinationVariable String roomId, 
            Map<String, String> messageMap,
            SimpMessageHeaderAccessor headerAccessor) {
        String sender = messageMap.get("sender");
        String receiver = messageMap.get("receiver");
        String content = messageMap.get("content");
        chatService.sendMessage(roomId, sender, receiver, content);
    }


    @SubscribeMapping("/chat/{roomId}")
    public List<ChatMessage> subscribeToRoom(@DestinationVariable String roomId) {
        return chatService.getMessagesByRoomId(roomId);
    }
    
    @MessageMapping("/chat/create/{sender}/{receiver}")
    @SendTo("/topic/rooms")
    public ChatRoom createRoom(
            @DestinationVariable String sender,
            @DestinationVariable String receiver) {
        
        ChatRoom room = chatService.getChatRoomByPacilianAndCaregiver(sender, receiver);
        return room; 
    }

    @MessageMapping("/chat/edit/{roomId}")
    public Optional<ChatMessage> editMessage(
        @DestinationVariable String roomId,
        EditChatRequest req
        ) {
        req.setRoomId(roomId);
        return chatService.editMessage(req.getRoomId(), req.getMessageId(), req.getContent());
    }

    @MessageMapping("/chat/delete/{roomId}")
    public void delete(
        @DestinationVariable String roomId,
        DeleteChatRequest req
        ) {
        req.setRoomId(roomId);
        chatService.deleteMessage(req.getRoomId(), req.getMessageId());
    }

}

