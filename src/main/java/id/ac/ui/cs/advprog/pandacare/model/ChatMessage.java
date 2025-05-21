package id.ac.ui.cs.advprog.pandacare.model;

import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.observer.ChatObserver;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
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
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom room;

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

    @Transient
    private final List<ChatObserver> observers = new ArrayList<>();

    public Long getRoomId() {
        return room != null ? room.getId() : null;
    }

    public Long getSenderId() {
        return sender != null ? sender.getId() : null;
    }

    public Long getReceiverId() {
        return receiver != null ? receiver.getId() : null;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    public void onUpdate() {
        updatedAt = LocalDateTime.now();
        notifyObservers("Message updated");
    }

    public void setContent(String content) {
        if (!initialized) {
            this.content = content;
            initialized = true;
        } else if (!Objects.equals(this.content, content)) {
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

    public void addObserver(ChatObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(ChatObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String event) {
        for (ChatObserver observer : new ArrayList<>(observers)) {
            observer.update(this, event);
        }
    }
}
