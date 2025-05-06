package id.ac.ui.cs.advprog.pandacare.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import id.ac.ui.cs.advprog.pandacare.enums.Role;
import id.ac.ui.cs.advprog.pandacare.model.User;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserBuilder() {
        User user = User.builder()
                .id(1L)
                .nik(12345)
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .address("123 Test St")
                .phonenum("08123456789")
                .role(Role.ADMIN)
                .build();

        assertEquals(1, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void testGetAuthorities() {
        User user = new User();
        user.setRole(Role.USER);

        Collection<? extends SimpleGrantedAuthority> authorities =
                (Collection<? extends SimpleGrantedAuthority>) user.getAuthorities();

        Assertions.assertEquals(1, authorities.size());
        Assertions.assertEquals("USER", authorities.iterator().next().getAuthority());
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
        User user = new User(1L, 12345, "Test User", "test@example.com",
                "password", "123 St", "08123", Role.USER);

        assertEquals(1, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = User.builder().id(1L).email("test@example.com").build();
        User user2 = User.builder().id(1L).email("test@example.com").build();
        User user3 = User.builder().id(2L).email("other@example.com").build();

        Assertions.assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        Assertions.assertNotEquals(user1, user3);
    }

    @Test
    void testToString() {
        User user = User.builder().id(1L).name("Test").role(Role.USER).build();
        String toString = user.toString();

        Assertions.assertTrue(toString.contains("User("));
        Assertions.assertTrue(toString.contains("id=1"));
        Assertions.assertTrue(toString.contains("name=Test"));
        Assertions.assertTrue(toString.contains("role=USER"));
    }
}