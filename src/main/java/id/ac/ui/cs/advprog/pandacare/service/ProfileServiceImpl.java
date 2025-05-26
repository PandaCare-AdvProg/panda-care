package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.dto.ConsultationHistoryDTO;
import id.ac.ui.cs.advprog.pandacare.dto.ProfileDTO;
import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import id.ac.ui.cs.advprog.pandacare.model.User;
import id.ac.ui.cs.advprog.pandacare.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.pandacare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final ConsultationRepository consultationRepository;
    
    @Override
    public ProfileDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return mapToProfileDTO(user);
    }
    
    @Override
    public ProfileDTO getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return mapToProfileDTO(user);
    }
    
    @Override
    @Transactional
    public ProfileDTO updateUserProfile(Long userId, ProfileDTO profileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return updateUserFields(user, profileDTO);
    }
    
    @Override
    @Transactional
    public ProfileDTO updateUserProfileByEmail(String email, ProfileDTO profileDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return updateUserFields(user, profileDTO);
    }
    
    @Override
    @Transactional
    public void deleteUserAccount(Long userId) {
        userRepository.deleteById(userId);
    }
    
    @Override
    @Transactional
    public void deleteUserAccountByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        userRepository.delete(user);
    }
    
    @Override
    public List<ConsultationHistoryDTO> getUserConsultationHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return getConsultationsByUser(user);
    }
    
    @Override
    public List<ConsultationHistoryDTO> getUserConsultationHistoryByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return getConsultationsByUser(user);
    }
    
    // Helper method for updating user fields
    private ProfileDTO updateUserFields(User user, ProfileDTO profileDTO) {
        // Update common fields
        user.setName(profileDTO.getName());
        user.setPhonenum(profileDTO.getPhonenum());
        
        // Type-specific updates
        if (user.getRole() == Role.PATIENT && user instanceof Patient) {
            Patient patient = (Patient) user;
            patient.setAddress(profileDTO.getAddress());
            patient.setMedicalHistory(profileDTO.getMedicalHistory());
        } else if (user.getRole() == Role.DOCTOR && user instanceof Doctor) {
            Doctor doctor = (Doctor) user;
            doctor.setWorkingAddress(profileDTO.getWorkingAddress());
            doctor.setSpecialty(profileDTO.getSpecialty());
            doctor.setWorkingAddress(profileDTO.getWorkingAddress());
        }
        
        User updatedUser = userRepository.save(user);
        return mapToProfileDTO(updatedUser);
    }
    
    private List<ConsultationHistoryDTO> getConsultationsByUser(User user) {
        List<Consultation> consultations = new ArrayList<>();
        
        if (user.getRole() == Role.PATIENT) {
            consultations = consultationRepository.findByPatientId(user.getId());
        } else if (user.getRole() == Role.DOCTOR) {
            consultations = consultationRepository.findByDoctorId(user.getId());
        }
        
        return consultations.stream()
                .map(this::mapToConsultationHistoryDTO)
                .collect(Collectors.toList());
    }
    
    private ProfileDTO mapToProfileDTO(User user) {
        ProfileDTO.ProfileDTOBuilder builder = ProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .nik(user.getNik())
                .address(user.getAddress())
                .phonenum(user.getPhonenum())
                .role(user.getRole());
        
        if (user instanceof Patient) {
            Patient patient = (Patient) user;
            builder.medicalHistory(patient.getMedicalHistory());
        } else if (user instanceof Doctor) {
            Doctor doctor = (Doctor) user;
            builder.specialty(doctor.getSpecialty())
                  .workingAddress(doctor.getWorkingAddress());
        }
        
        return builder.build();
    }
    
    private ConsultationHistoryDTO mapToConsultationHistoryDTO(Consultation consultation) {
        return ConsultationHistoryDTO.builder()
                .id(consultation.getId())
                .doctorName(consultation.getDoctor().getName())
                .patientName(consultation.getPatient().getName())
                .dayOfWeek(consultation.getDayOfWeek())
                .scheduledTime(consultation.getScheduledTime())
                .notes(consultation.getNotes())
                .status(consultation.getStatus())
                .build();
    }
}