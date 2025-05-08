package id.ac.ui.cs.advprog.pandacare.model;

import id.ac.ui.cs.advprog.pandacare.enums.TokenType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

    @Test
    void testTokenBuilder() {
        Token token = Token.builder()
                .id(1)
                .token("sample-token")
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();

        assertEquals(1, token.getId());
        assertEquals("sample-token", token.getToken());
        assertEquals(TokenType.BEARER, token.getTokenType());
        assertFalse(token.isRevoked());
        assertFalse(token.isExpired());
    }

    @Test
    void testTokenSettersAndGetters() {
        Token token = new Token();
        token.setId(2);
        token.setToken("another-token");
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(true);
        token.setExpired(true);

        assertEquals(2, token.getId());
        assertEquals("another-token", token.getToken());
        assertEquals(TokenType.BEARER, token.getTokenType());
        assertTrue(token.isRevoked());
        assertTrue(token.isExpired());
    }

    @Test
    void testTokenEquality() {
        Token token1 = Token.builder()
                .id(1)
                .token("sample-token")
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();

        Token token2 = Token.builder()
                .id(1)
                .token("sample-token")
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();

        assertEquals(token1, token2);
    }

    @Test
    void testTokenNotEqual() {
        Token token1 = Token.builder()
                .id(1)
                .token("sample-token")
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();

        Token token2 = Token.builder()
                .id(2)
                .token("different-token")
                .tokenType(TokenType.BEARER)
                .revoked(true)
                .expired(true)
                .build();

        assertNotEquals(token1, token2);
    }
}