package id.ac.ui.cs.advprog.pandacare.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.enums.TokenType;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.model.Patient;
import id.ac.ui.cs.advprog.pandacare.model.Token;
import id.ac.ui.cs.advprog.pandacare.model.User;
import id.ac.ui.cs.advprog.pandacare.repository.TokenRepository;
import id.ac.ui.cs.advprog.pandacare.repository.UserRepository;
import id.ac.ui.cs.advprog.pandacare.request.AuthenticationRequest;
import id.ac.ui.cs.advprog.pandacare.request.RegisterRequest;
import id.ac.ui.cs.advprog.pandacare.response.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private LogoutHandler logoutHandler;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----- Existing tests (testRegister and testAuthenticate) -----

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password");
        request.setRole(Role.USER);

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.register(request);

        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(userRepository, times(1)).save(any(User.class));
        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("john.doe@example.com");
        request.setPassword("password");

        User user = User.builder()
                .id(1L)
                .email(request.getEmail())
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        // Simulate no valid tokens so that revokeAllUserTokens backs out
        when(tokenRepository.findAllValidTokenByUser(anyInt())).thenReturn(Collections.emptyList());

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(tokenRepository, times(1)).save(any());
    }

    // ----- New tests for uncovered branches -----

    @Test
    void testRegisterDoctor() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Dr. Bob");
        request.setEmail("bob@example.com");
        request.setPassword("doctorpass");
        request.setRole(Role.DOCTOR);
        request.setNik("123456789");
        request.setAddress("Doctor Address");
        request.setWorkingAddress("Hospital XYZ");
        request.setPhonenum("0800000000");
        request.setSpecialty("Neurology");

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedDoctorPass");
        // Simulate repository.save returning the same doctor instance
        when(userRepository.save(any(Doctor.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtDoctorToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshDoctorToken");

        AuthenticationResponse response = authenticationService.register(request);

        assertEquals("jwtDoctorToken", response.getAccessToken());
        assertEquals("refreshDoctorToken", response.getRefreshToken());
        verify(userRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void testRegisterPatient() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Patient Jane");
        request.setEmail("jane@example.com");
        request.setPassword("patientpass");
        request.setRole(Role.PATIENT);
        request.setNik("987654321");
        request.setAddress("Patient Address");
        request.setPhonenum("0811111111");
        request.setMedicalHistory("No allergies");

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPatientPass");
        when(userRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtPatientToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshPatientToken");

        AuthenticationResponse response = authenticationService.register(request);

        assertEquals("jwtPatientToken", response.getAccessToken());
        assertEquals("refreshPatientToken", response.getRefreshToken());
        verify(userRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testRevokeAllUserTokensWithExistingTokens() throws IOException {
        // Prepare a user with an existing valid token
        User user = User.builder().id(2L).build();
        Token token = Token.builder()
                .token("oldToken")
                .expired(false)
                .revoked(false)
                .tokenType(TokenType.BEARER)
                .user(user)
                .build();
        List<Token> tokens = List.of(token);
        when(tokenRepository.findAllValidTokenByUser(user.getId().intValue())).thenReturn(tokens);

        // Call a method that will invoke revokeAllUserTokens (e.g. refreshToken valid branch)
        // Using refreshToken we can indirectly call the method:
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer refreshTokenValue");
        when(jwtService.extractUsername("refreshTokenValue")).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid("refreshTokenValue", user)).thenReturn(false);
        // When token is invalid, nothing further happens.
        authenticationService.refreshToken(request, response);
        // Verify that the valid token was fetched and then revoked and saved.
        tokens.forEach(t -> {
            assertTrue(t.isExpired());
            assertTrue(t.isRevoked());
        });
        verify(tokenRepository, times(1)).saveAll(tokens);
    }

    @Test
    void testRefreshTokenNoAuthHeader() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // when no auth header is provided.
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        // Calling refreshToken should return immediately (nothing written)
        authenticationService.refreshToken(request, response);
        // Verify that jwtService.extractUsername is never called.
        verify(jwtService, never()).extractUsername(any());
        verify(userRepository, never()).findByEmail(any());
    }

    @Test
    void testRefreshTokenInvalidAuthHeader() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Provide auth header that does not start with "Bearer "
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("InvalidHeader");

        authenticationService.refreshToken(request, response);
        verify(jwtService, never()).extractUsername(any());
        verify(userRepository, never()).findByEmail(any());
    }

    @Test
    void testRefreshTokenWithInvalidToken() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer someInvalidToken");
        when(jwtService.extractUsername("someInvalidToken")).thenReturn("nonexistent@example.com");
        when(userRepository.findByEmail("nonexistent@example.com")).thenThrow(new RuntimeException("User not found"));

        // Expect an exception since userRepository.findByEmail throws.
        assertThrows(RuntimeException.class, () -> 
                authenticationService.refreshToken(request, response));
    }

    @Test
    void testLogout() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        authenticationService.logout(request, response);

        verify(logoutHandler, times(1)).logout(request, response, auth);
    }
}