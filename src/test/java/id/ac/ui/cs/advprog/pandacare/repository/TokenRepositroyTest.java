package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.enums.TokenType;
import id.ac.ui.cs.advprog.pandacare.model.Token;
import id.ac.ui.cs.advprog.pandacare.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenRepositoryTest {

    @Mock
    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllValidTokenByUser() {
        User user = new User();
        user.setId(1L);

        Token validToken = new Token();
        validToken.setToken("valid");
        validToken.setTokenType(TokenType.BEARER);
        validToken.setExpired(false);
        validToken.setRevoked(false);
        validToken.setUser(user);

        when(tokenRepository.findAllValidTokenByUser(1)).thenReturn(List.of(validToken));

        List<Token> tokens = tokenRepository.findAllValidTokenByUser(1);
        assertEquals(1, tokens.size());
        assertEquals("valid", tokens.get(0).getToken());
    }

    @Test
    void testFindByToken() {
        Token token = new Token();
        token.setToken("mytoken");
        token.setExpired(false);
        token.setRevoked(false);

        when(tokenRepository.findByToken("mytoken")).thenReturn(Optional.of(token));

        Optional<Token> found = tokenRepository.findByToken("mytoken");
        assertTrue(found.isPresent());
        assertEquals("mytoken", found.get().getToken());
    }

    @Test
    void testFindByTokenNotFound() {
        when(tokenRepository.findByToken("doesNotExist")).thenReturn(Optional.empty());

        Optional<Token> found = tokenRepository.findByToken("doesNotExist");
        assertFalse(found.isPresent());
    }
}