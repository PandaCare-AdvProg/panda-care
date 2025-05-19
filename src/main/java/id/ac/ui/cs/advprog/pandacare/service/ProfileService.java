package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.dto.ConsultationHistoryDTO;
import id.ac.ui.cs.advprog.pandacare.dto.ProfileDTO;

import java.util.List;

public interface ProfileService {
    // ID-based methods
    ProfileDTO getUserProfile(Long userId);
    ProfileDTO updateUserProfile(Long userId, ProfileDTO profileDTO);
    void deleteUserAccount(Long userId);
    List<ConsultationHistoryDTO> getUserConsultationHistory(Long userId);
    
    // Email-based methods
    ProfileDTO getUserProfileByEmail(String email);
    ProfileDTO updateUserProfileByEmail(String email, ProfileDTO profileDTO);
    void deleteUserAccountByEmail(String email);
    List<ConsultationHistoryDTO> getUserConsultationHistoryByEmail(String email);
}