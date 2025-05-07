package id.ac.ui.cs.advprog.pandacare.model;

import java.time.LocalDateTime;

public class ChatMessage {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt = null;
    
    public ChatMessage() {
    }
    
    public ChatMessage(String content) {
        this.content = content;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}