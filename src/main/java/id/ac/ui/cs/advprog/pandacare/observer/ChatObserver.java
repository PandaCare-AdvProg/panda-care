package id.ac.ui.cs.advprog.pandacare.observer;

import id.ac.ui.cs.advprog.pandacare.model.ChatMessage;

public interface ChatObserver {
    void update(ChatMessage chatMessage, String event);
}
