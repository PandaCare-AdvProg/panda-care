package id.ac.ui.cs.advprog.pandacare.observer;

import id.ac.ui.cs.advprog.pandacare.model.Consultation;

public interface ConsultationObserver {
    void update(Consultation consultation, String message);
}