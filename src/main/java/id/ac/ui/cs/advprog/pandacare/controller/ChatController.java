package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.dto.ApiResponse;
import id.ac.ui.cs.advprog.pandacare.dto.ChatMessageDTO;
import id.ac.ui.cs.advprog.pandacare.dto.ChatRequest;
import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;
import id.ac.ui.cs.advprog.pandacare.model.ChatRoom;
import id.ac.ui.cs.advprog.pandacare.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/rooms")
    public ResponseEntity<ApiResponse<ChatRoom>> createRoom(@RequestParam Long doctorId, @RequestParam Long patientId) {
        ChatRoom room = chatService.createRoom(doctorId, patientId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Chat room created successfully", room));
    }

    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse<List<ChatRoom>>> getUserRooms() {
        Long userId = getCurrentUserId();
        List<ChatRoom> rooms = chatService.getRoomsByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Chat rooms retrieved successfully", rooms));
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ApiResponse<ChatRoom>> getRoomById(@PathVariable Long roomId) {
        ChatRoom room = chatService.getRoomById(roomId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Chat room retrieved successfully", room));
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageDTO>>> getRoomMessages(@PathVariable Long roomId) {
        List<ChatMessage> messages = chatService.getRoomMessages(roomId);
        List<ChatMessageDTO> messageDTOs = chatService.mapToDTOList(messages);
        return ResponseEntity.ok(new ApiResponse<>(true, "Messages retrieved successfully", messageDTOs));
    }

    @PostMapping("/messages")
    public ResponseEntity<ApiResponse<ChatMessageDTO>> sendMessage(@RequestBody ChatRequest request) {
        Long senderId = getCurrentUserId();
        ChatMessage message = chatService.sendMessage(request.getRoomId(), senderId, request.getContent());
        ChatMessageDTO messageDTO = chatService.mapToDTO(message);
        return ResponseEntity.ok(new ApiResponse<>(true, "Message sent successfully", messageDTO));
    }

    @PutMapping("/messages/{messageId}")
    public ResponseEntity<ApiResponse<ChatMessageDTO>> editMessage(
            @PathVariable Long messageId,
            @RequestBody ChatRequest request) {
        Long senderId = getCurrentUserId();
        ChatMessage message = chatService.editMessage(messageId, senderId, request.getContent());
        ChatMessageDTO messageDTO = chatService.mapToDTO(message);
        return ResponseEntity.ok(new ApiResponse<>(true, "Message edited successfully", messageDTO));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<ApiResponse<ChatMessageDTO>> deleteMessage(@PathVariable Long messageId) {
        Long senderId = getCurrentUserId();
        ChatMessage message = chatService.deleteMessage(messageId, senderId);
        ChatMessageDTO messageDTO = chatService.mapToDTO(message);
        return ResponseEntity.ok(new ApiResponse<>(true, "Message deleted successfully", messageDTO));
    }
    
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userIdStr = auth.getName();
        
        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            // If using email-based authentication, you'll need additional logic here
            throw new IllegalArgumentException("User identification not supported");
        }
    }

    public ResponseEntity<ApiResponse<ChatMessageDTO>> editMessage(long l, ChatRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editMessage'");
    }
}