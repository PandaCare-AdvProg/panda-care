package id.ac.ui.cs.advprog.pandacare.service;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final LogoutHandler logoutHandler; 

public AuthenticationResponse register(RegisterRequest request) {
    User savedUser;
    if (request.getRole() == Role.DOCTOR) {
        var doctor = new Doctor(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getName(),
            request.getNik(),
            request.getAddress(),
            request.getWorkingAddress(), 
            request.getPhonenum(),
            Role.DOCTOR,
            request.getSpecialty()
        );
        savedUser = repository.save(doctor);
    } else if (request.getRole() == Role.PATIENT) {
        var patient = new Patient(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getName(),
            request.getNik(),
            request.getAddress(),
            request.getPhonenum(),
            Role.PATIENT,
            request.getMedicalHistory()
        );
        savedUser = repository.save(patient);
    } else {
        var user = new User(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getName(),
            request.getNik(),
            request.getAddress(),
            request.getPhonenum(),
            request.getRole()
        );
        savedUser = repository.save(user);
    }

    var jwtToken = jwtService.generateToken(savedUser);
    var refreshToken = jwtService.generateRefreshToken(savedUser);
    saveUserToken(savedUser, jwtToken);

    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .build();
}

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = (List<Token>) tokenRepository.findAllValidTokenByUser(user.getId().intValue());
    if (validUserTokens == null || validUserTokens.isEmpty()) {
      return;
    }
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  public void logout(HttpServletRequest request, HttpServletResponse response) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    logoutHandler.logout(request, response, authentication);
  }
}