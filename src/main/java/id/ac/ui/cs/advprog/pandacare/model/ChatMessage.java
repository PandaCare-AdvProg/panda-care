package id.ac.ui.cs.advprog.pandacare.model;

import id.ac.ui.cs.advprog.pandacare.model.User;

import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.observer.ChatObserver;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_role")
    private Role senderRole;

    private String content;

    @Builder.Default
    private boolean edited = false;

    @Builder.Default
    private boolean deleted = false;

    @Builder.Default
    private boolean initialized = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getSenderId() {
        return sender != null ? sender.getId() : null;
    }

    public Long getReceiverId() {
        return receiver != null ? receiver.getId() : null;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    void onUpdate() {
        updatedAt = LocalDateTime.now();
        notifyObservers("Message updated");
    }

    // Observer pattern implementation
    @Transient
    private final List<ChatObserver> observers = new ArrayList<>();

    public void addObserver(ChatObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(ChatObserver observer) {
        observers.remove(observer);
    }

    void notifyObservers(String event) {
        for (ChatObserver observer : new ArrayList<>(observers)) {
            observer.update(this, event);
        }
    }

    public void setContent(String content) {
        if (!initialized) {
            this.content = content;
            initialized = true;
            return;
        }

        if (!Objects.equals(this.content, content)) {
            this.content = content;
            if (id != null) {
                this.edited = true;
                notifyObservers("Content updated");
            }
        }
    }

    public void markAsDeleted() {
        this.deleted = true;
        notifyObservers("Message deleted");
    }
}
