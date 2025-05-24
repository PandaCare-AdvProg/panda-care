package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.dto.ApiResponse;
import id.ac.ui.cs.advprog.pandacare.dto.ConsultationHistoryDTO;
import id.ac.ui.cs.advprog.pandacare.dto.ProfileDTO;
import id.ac.ui.cs.advprog.pandacare.dto.ProfileUpdateRequest;
import id.ac.ui.cs.advprog.pandacare.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    private ProfileDTO mockProfileDTO;
    private ConsultationHistoryDTO mockConsultationDTO;
    private ProfileUpdateRequest mockUpdateRequest;

    @BeforeEach
    void setUp() {
        mockProfileDTO = ProfileDTO.builder()
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .address("Test Address")
                .phonenum("1234567890")
                .medicalHistory("No history")
                .build();

        mockUpdateRequest = new ProfileUpdateRequest();
        mockUpdateRequest.setEmail("updated@example.com");
        mockUpdateRequest.setName("Updated User");
        mockUpdateRequest.setAddress("Updated Address");
        mockUpdateRequest.setPhonenum("0987654321");
        mockUpdateRequest.setMedicalHistory("Updated history");
    }

    private void setAuthenticationContext(String principal) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetUserProfile_WithNumericId() {
        setAuthenticationContext("1");
        
        when(profileService.getUserProfile(1L)).thenReturn(mockProfileDTO);
        
        ResponseEntity<ApiResponse<ProfileDTO>> response = profileController.getUserProfile();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Profile retrieved successfully", response.getBody().getMessage());
        assertEquals(mockProfileDTO, response.getBody().getData());
    }

    @Test
    void testGetUserProfile_WithEmail() {
        setAuthenticationContext("test@example.com");
        
        when(profileService.getUserProfileByEmail("test@example.com")).thenReturn(mockProfileDTO);
        
        ResponseEntity<ApiResponse<ProfileDTO>> response = profileController.getUserProfile();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Profile retrieved successfully", response.getBody().getMessage());
        assertEquals(mockProfileDTO, response.getBody().getData());
    }

    @Test
    void testGetUserProfile_InvalidIdentifier() {
        setAuthenticationContext("invalid");
        
        ResponseEntity<ApiResponse<ProfileDTO>> response = profileController.getUserProfile();
        
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Invalid user identifier", response.getBody().getMessage());
    }

    @Test
    void testUpdateUserProfile_WithNumericId() {
        setAuthenticationContext("1");
        
        ProfileDTO updatedProfile = ProfileDTO.builder()
                .email("updated@example.com")
                .name("Updated User")
                .address("Updated Address")
                .phonenum("0987654321")
                .medicalHistory("Updated history")
                .build();
        
        when(profileService.updateUserProfile(1L, updatedProfile)).thenReturn(updatedProfile);
        
        ResponseEntity<ApiResponse<ProfileDTO>> response = profileController.updateUserProfile(mockUpdateRequest);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Profile updated successfully", response.getBody().getMessage());
        assertEquals(updatedProfile, response.getBody().getData());
    }

    @Test
    void testUpdateUserProfile_WithEmail() {
        setAuthenticationContext("test@example.com");
        
        ProfileDTO updatedProfile = ProfileDTO.builder()
                .email("updated@example.com")
                .name("Updated User")
                .address("Updated Address")
                .phonenum("0987654321")
                .medicalHistory("Updated history")
                .build();
        
        when(profileService.updateUserProfileByEmail("test@example.com", updatedProfile)).thenReturn(updatedProfile);
        
        ResponseEntity<ApiResponse<ProfileDTO>> response = profileController.updateUserProfile(mockUpdateRequest);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Profile updated successfully", response.getBody().getMessage());
        assertEquals(updatedProfile, response.getBody().getData());
    }

    @Test
    void testDeleteUserAccount_WithNumericId() {
        setAuthenticationContext("1");
        
        ResponseEntity<ApiResponse<Void>> response = profileController.deleteUserAccount();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Account deleted successfully", response.getBody().getMessage());
        
        verify(profileService, times(1)).deleteUserAccount(1L);
    }

    @Test
    void testDeleteUserAccount_WithEmail() {
        setAuthenticationContext("test@example.com");
        
        ResponseEntity<ApiResponse<Void>> response = profileController.deleteUserAccount();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Account deleted successfully", response.getBody().getMessage());
        
        verify(profileService, times(1)).deleteUserAccountByEmail("test@example.com");
    }

    @Test
    void testGetUserConsultationHistory_WithNumericId() {
        setAuthenticationContext("1");
        List<ConsultationHistoryDTO> mockHistory = Collections.singletonList(mockConsultationDTO);
        
        when(profileService.getUserConsultationHistory(1L)).thenReturn(mockHistory);
        
        ResponseEntity<ApiResponse<List<ConsultationHistoryDTO>>> response = 
            profileController.getUserConsultationHistory();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Consultation history retrieved successfully", response.getBody().getMessage());
        assertEquals(mockHistory, response.getBody().getData());
    }

    @Test
    void testGetUserConsultationHistory_WithEmail() {
        setAuthenticationContext("test@example.com");
        List<ConsultationHistoryDTO> mockHistory = Collections.singletonList(mockConsultationDTO);
        
        when(profileService.getUserConsultationHistoryByEmail("test@example.com")).thenReturn(mockHistory);
        
        ResponseEntity<ApiResponse<List<ConsultationHistoryDTO>>> response = 
            profileController.getUserConsultationHistory();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Consultation history retrieved successfully", response.getBody().getMessage());
        assertEquals(mockHistory, response.getBody().getData());
    }
}