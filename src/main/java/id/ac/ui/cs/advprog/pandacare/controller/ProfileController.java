package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.dto.ConsultationHistoryDTO;
import id.ac.ui.cs.advprog.pandacare.dto.ProfileDTO;
import id.ac.ui.cs.advprog.pandacare.dto.ProfileUpdateRequest;
import id.ac.ui.cs.advprog.pandacare.dto.ApiResponse;
import id.ac.ui.cs.advprog.pandacare.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileDTO>> getUserProfile() {
        // Get authentication principal (username) - which might be email or ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userIdentifier = authentication.getName();
        
        ProfileDTO profileDTO;
        
        if (userIdentifier.contains("@")) {
            // Handle as email - you'll need a method in your service
            profileDTO = profileService.getUserProfileByEmail(userIdentifier);
        } else {
            try {
                // Handle as numeric ID
                Long userId = Long.parseLong(userIdentifier);
                profileDTO = profileService.getUserProfile(userId);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "Invalid user identifier", null)
                );
            }
        }
        
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Profile retrieved successfully", profileDTO)
        );
    }
    
    @PutMapping
    public ResponseEntity<ApiResponse<ProfileDTO>> updateUserProfile(
            @RequestBody ProfileUpdateRequest request) {
        
        // Get authentication principal (username)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userIdentifier = authentication.getName();
        
        // Convert request to DTO
        ProfileDTO profileDTO = ProfileDTO.builder()
                .email(request.getEmail())
                .name(request.getName())
                .address(request.getAddress())
                .phonenum(request.getPhonenum())
                .medicalHistory(request.getMedicalHistory())
                .specialty(request.getSpecialty())
                .workingAddress(request.getWorkingAddress())
                .build();
        
        ProfileDTO updatedProfile;
        
        // Handle email or numeric ID
        if (userIdentifier.contains("@")) {
            updatedProfile = profileService.updateUserProfileByEmail(userIdentifier, profileDTO);
        } else {
            try {
                Long userId = Long.parseLong(userIdentifier);
                updatedProfile = profileService.updateUserProfile(userId, profileDTO);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "Invalid user identifier", null)
                );
            }
        }
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully", updatedProfile));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUserAccount() {
        // Get authentication principal (username)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userIdentifier = authentication.getName();
        
        // Handle email or numeric ID
        if (userIdentifier.contains("@")) {
            profileService.deleteUserAccountByEmail(userIdentifier);
        } else {
            try {
                Long userId = Long.parseLong(userIdentifier);
                profileService.deleteUserAccount(userId);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "Invalid user identifier", null)
                );
            }
        }
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Account deleted successfully", null));
    }

    @GetMapping("/consultations")
    public ResponseEntity<ApiResponse<List<ConsultationHistoryDTO>>> getUserConsultationHistory() {
        // Get authentication principal (username)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userIdentifier = authentication.getName();
        
        List<ConsultationHistoryDTO> history;
        
        // Handle email or numeric ID
        if (userIdentifier.contains("@")) {
            history = profileService.getUserConsultationHistoryByEmail(userIdentifier);
        } else {
            try {
                Long userId = Long.parseLong(userIdentifier);
                history = profileService.getUserConsultationHistory(userId);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "Invalid user identifier", null)
                );
            }
        }
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Consultation history retrieved successfully", history));
    }
}
