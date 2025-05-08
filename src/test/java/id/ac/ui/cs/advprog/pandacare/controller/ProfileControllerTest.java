package id.ac.ui.cs.advprog.pandacare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.pandacare.dto.ApiResponse;
import id.ac.ui.cs.advprog.pandacare.dto.ConsultationHistoryDTO;
import id.ac.ui.cs.advprog.pandacare.dto.ProfileDTO;
import id.ac.ui.cs.advprog.pandacare.dto.ProfileUpdateRequest;
import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfileService profileService;

    private UserDetails createUserDetails(String userId) {
        return User.builder()
                .username(userId)
                .password("password")
                .authorities(Collections.emptyList())
                .build();
    }

    @Test
    @WithMockUser(username = "1")
    void getUserProfile_returnsProfileDTO() throws Exception {
        // Arrange
        ProfileDTO profileDTO = ProfileDTO.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role(Role.PATIENT)
                .build();

        when(profileService.getUserProfile(1L)).thenReturn(profileDTO);

        // Act & Assert
        mockMvc.perform(get("/api/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Profile retrieved successfully")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Test User")))
                .andExpect(jsonPath("$.data.email", is("test@example.com")));
    }

    @Test
    @WithMockUser(username = "1")
    void updateUserProfile_validRequest_returnsUpdatedProfile() throws Exception {
        // Arrange
        ProfileUpdateRequest request = ProfileUpdateRequest.builder()
                .name("Updated Name")
                .email("updated@example.com")
                .address("Updated Address")
                .phonenum("123456789")
                .medicalHistory("Updated history")
                .build();

        ProfileDTO updatedProfile = ProfileDTO.builder()
                .id(1L)
                .name("Updated Name")
                .email("updated@example.com")
                .address("Updated Address")
                .phonenum("123456789")
                .medicalHistory("Updated history")
                .role(Role.PATIENT)
                .build();

        when(profileService.updateUserProfile(eq(1L), any(ProfileDTO.class))).thenReturn(updatedProfile);

        // Act & Assert
        mockMvc.perform(put("/api/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Profile updated successfully")))
                .andExpect(jsonPath("$.data.name", is("Updated Name")))
                .andExpect(jsonPath("$.data.email", is("updated@example.com")))
                .andExpect(jsonPath("$.data.address", is("Updated Address")));
    }

    @Test
    @WithMockUser(username = "1")
    void deleteUserAccount_validRequest_returnsSuccess() throws Exception {
        // Arrange
        doNothing().when(profileService).deleteUserAccount(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Account deleted successfully")));
    }

    @Test
    @WithMockUser(username = "1")
    void getUserConsultationHistory_returnsHistory() throws Exception {
        // Arrange
        List<ConsultationHistoryDTO> history = Arrays.asList(
                ConsultationHistoryDTO.builder()
                        .id(1L)
                        .doctorName("Dr. Smith")
                        .patientName("Test Patient")
                        .notes("Regular checkup")
                        .build(),
                ConsultationHistoryDTO.builder()
                        .id(2L)
                        .doctorName("Dr. Jones")
                        .patientName("Test Patient")
                        .notes("Follow-up")
                        .build()
        );

        when(profileService.getUserConsultationHistory(1L)).thenReturn(history);

        // Act & Assert
        mockMvc.perform(get("/api/profile/consultations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Consultation history retrieved successfully")))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].doctorName", is("Dr. Smith")))
                .andExpect(jsonPath("$.data[1].id", is(2)))
                .andExpect(jsonPath("$.data[1].doctorName", is("Dr. Jones")));
    }

    @Test
    @WithMockUser(username = "invalid")
    void getUserProfile_invalidUserId_returnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}