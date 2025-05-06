package id.ac.ui.cs.advprog.pandacare.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import id.ac.ui.cs.advprog.pandacare.repository.TokenRepository;
import id.ac.ui.cs.advprog.pandacare.model.Token;
import java.util.Optional;
import static org.mockito.Mockito.*;

class LogoutServiceTest {

    private LogoutService logoutService;
    private TokenRepository tokenRepository;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        tokenRepository = mock(TokenRepository.class);
        logoutService = new LogoutService(tokenRepository);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        authentication = mock(Authentication.class);
    }

    @Test
    void testLogoutWithValidToken() {
        String jwt = "validToken";
        Token token = new Token();
        token.setExpired(false);
        token.setRevoked(false);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(tokenRepository.findByToken(jwt)).thenReturn(Optional.of(token));

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, times(1)).findByToken(jwt);
        verify(tokenRepository, times(1)).save(token);
        verify(tokenRepository).save(argThat(savedToken -> savedToken.isExpired() && savedToken.isRevoked()));
        verifyNoMoreInteractions(tokenRepository);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testLogoutWithInvalidToken() {
        String jwt = "invalidToken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(tokenRepository.findByToken(jwt)).thenReturn(Optional.empty());

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, times(1)).findByToken(jwt);
        verifyNoMoreInteractions(tokenRepository);
    }

    @Test
    void testLogoutWithNoAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        logoutService.logout(request, response, authentication);

        verifyNoInteractions(tokenRepository);
    }

    @Test
    void testLogoutWithMalformedAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        logoutService.logout(request, response, authentication);

        verifyNoInteractions(tokenRepository);
    }
}