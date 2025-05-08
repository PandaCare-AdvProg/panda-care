package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import java.util.List;

public interface ConsultationService {
    Consultation createConsultation(Consultation consultation);
    Consultation updateConsultation(Long consultationId, Consultation updatedConsultation);
    List<Consultation> getConsultationsByDoctor(Long doctorId);
    List<Consultation> getConsultationsByPatient(Long patientId);
    Consultation approveConsultation(Long consultationId);
    Consultation rejectConsultation(Long consultationId);
    Consultation completeConsultation(Long consultationId);
    
}