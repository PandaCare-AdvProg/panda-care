package id.ac.ui.cs.advprog.pandacare.Auth;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserBuilder() {
        User user = User.builder()
                .id(1)
                .nik(12345)
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .address("123 Test St")
                .phonenum("08123456789")
                .role("ADMIN")
                .build();

        assertEquals(1, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    void testGetAuthorities() {
        User user = new User();
        user.setRole("USER");

        Collection<? extends SimpleGrantedAuthority> authorities =
                (Collection<? extends SimpleGrantedAuthority>) user.getAuthorities();

        Assertions.assertEquals(1, authorities.size());
        Assertions.assertEquals("ROLE_USER", authorities.iterator().next().getAuthority());
    }

    @Test
    void testGetUsernameReturnsEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        assertEquals("test@example.com", user.getUsername());
    }

    @Test
    void testAccountStatusMethods() {
        User user = new User();

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testNoArgsConstructor() {
        User user = new User();

        Assertions.assertNull(user.getId());
        Assertions.assertNull(user.getName());
        Assertions.assertNull(user.getRole());
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User(1, 12345, "Test User", "test@example.com",
                "password", "123 St", "08123", "USER");

        assertEquals(1, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("USER", user.getRole());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = User.builder().id(1).email("test@example.com").build();
        User user2 = User.builder().id(1).email("test@example.com").build();
        User user3 = User.builder().id(2).email("other@example.com").build();

        Assertions.assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        Assertions.assertNotEquals(user1, user3);
    }

    @Test
    void testToString() {
        User user = User.builder().id(1).name("Test").build();
        String toString = user.toString();

        Assertions.assertTrue(toString.contains("User("));
        Assertions.assertTrue(toString.contains("id=1"));
        Assertions.assertTrue(toString.contains("name=Test"));
    }
}