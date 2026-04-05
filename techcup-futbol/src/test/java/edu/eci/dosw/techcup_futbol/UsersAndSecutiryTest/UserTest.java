package edu.eci.dosw.techcup_futbol.UsersAndSecutiryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.User;

class UserTest {

    private DummyUser user;

    @BeforeEach
    void setUp() {
        // Arrange: Create a concrete dummy user to test base abstract behavior
        user = new DummyUser(1, "Base User", "BASE@MAIL.COM", "123456");
    }

    // --- Tests for constructor ---

    @Test
    void shouldCreateUserSuccessfully() {
        assertEquals(1, user.getId());
        assertEquals("Base User", user.getName());
        assertEquals("base@mail.com", user.getEmail());
        assertEquals("123456", user.getPassword());
    }

    // --- Tests for getName()/setName(String name) ---

    @Test
    void shouldSetAndGetNameSuccessfully() {
        user.setName("Updated Name");
        assertEquals("Updated Name", user.getName());
    }

    // --- Tests for getEmail()/setEmail(String email) ---

    @Test
    void shouldNormalizeEmailWhenSettingAndGettingEmail() {
        user.setEmail("  NEW.USER@MAIL.COM  ");
        assertEquals("new.user@mail.com", user.getEmail());
    }

    // --- Tests for getPassword()/setPassword(String password) ---

    @Test
    void shouldSetAndGetPasswordSuccessfully() {
        user.setPassword("newSecurePass");
        assertEquals("newSecurePass", user.getPassword());
    }

    // --- Tests for getId() ---

    @Test
    void shouldReturnIdSuccessfully() {
        assertEquals(1, user.getId());
    }

    private static class DummyUser extends User {
        DummyUser(int id, String name, String email, String password) {
            super(id, name, email, password);
        }
    }
}
