package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.dto.ConsultationHistoryDTO;
import id.ac.ui.cs.advprog.pandacare.dto.ProfileDTO;
import id.ac.ui.cs.advprog.pandacare.dto.ProfileUpdateRequest;
import id.ac.ui.cs.advprog.pandacare.dto.ApiResponse;
import id.ac.ui.cs.advprog.pandacare.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileDTO>> getUserProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = Long.parseLong(userDetails.getUsername());
        ProfileDTO profile = profileService.getUserProfile(userId);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile retrieved successfully", profile));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProfileDTO>> updateUserProfile(
            @RequestBody ProfileUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = Long.parseLong(userDetails.getUsername());
        
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
        
        ProfileDTO updatedProfile = profileService.updateUserProfile(userId, profileDTO);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully", updatedProfile));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUserAccount(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = Long.parseLong(userDetails.getUsername());
        profileService.deleteUserAccount(userId);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Account deleted successfully", null));
    }

    @GetMapping("/consultations")
    public ResponseEntity<ApiResponse<List<ConsultationHistoryDTO>>> getUserConsultationHistory(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = Long.parseLong(userDetails.getUsername());
        List<ConsultationHistoryDTO> history = profileService.getUserConsultationHistory(userId);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Consultation history retrieved successfully", history));
    }
}