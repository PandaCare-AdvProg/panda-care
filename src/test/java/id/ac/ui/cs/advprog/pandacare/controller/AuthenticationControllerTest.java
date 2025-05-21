package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.request.AuthenticationRequest;
import id.ac.ui.cs.advprog.pandacare.request.RegisterRequest;
import id.ac.ui.cs.advprog.pandacare.response.AuthenticationResponse;
import id.ac.ui.cs.advprog.pandacare.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class AuthenticationControllerTest {

        private AuthenticationService authenticationService;
        private AuthenticationController authenticationController;

        @BeforeEach
        void setUp() {
                authenticationService = mock(AuthenticationService.class);
                authenticationController = new AuthenticationController(authenticationService);
        }

        @Test
        void testRegister() {
                RegisterRequest registerRequest = new RegisterRequest();
                AuthenticationResponse expectedResponse = new AuthenticationResponse();

                when(authenticationService.register(registerRequest)).thenReturn(expectedResponse);

                ResponseEntity<AuthenticationResponse> response = authenticationController.register(registerRequest);

                assertEquals(expectedResponse, response.getBody());
                verify(authenticationService, times(1)).register(registerRequest);
        }

        @Test
        void testAuthenticate() {
                AuthenticationRequest authenticationRequest = new AuthenticationRequest();
                AuthenticationResponse expectedResponse = new AuthenticationResponse();

                when(authenticationService.authenticate(authenticationRequest)).thenReturn(expectedResponse);

                ResponseEntity<AuthenticationResponse> response = authenticationController.authenticate(authenticationRequest);

                assertEquals(expectedResponse, response.getBody());
                verify(authenticationService, times(1)).authenticate(authenticationRequest);
        }

        @Test
        void testRefreshToken() throws IOException {
                HttpServletRequest request = mock(HttpServletRequest.class);
                HttpServletResponse response = mock(HttpServletResponse.class);

                doNothing().when(authenticationService).refreshToken(request, response);

                authenticationController.refreshToken(request, response);

                verify(authenticationService, times(1)).refreshToken(request, response);
        }
        @Test
        void testLogout() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    doNothing().when(authenticationService).logout(request, response);

    ResponseEntity<String> result = authenticationController.logout(request, response);

    assertEquals("Logged out successfully", result.getBody());
    verify(authenticationService, times(1)).logout(request, response);
}
} 