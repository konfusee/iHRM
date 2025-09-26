package com.ihrm.web.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    void testPasswordHashing() {
        String password = "mySecurePassword123";

        // Hash the password
        String hashedPassword = PasswordUtil.hashPassword(password);

        // Verify the format
        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.startsWith("$SHA256$"));

        // Verify password verification works
        assertTrue(PasswordUtil.verifyPassword(password, hashedPassword));

        // Verify wrong password fails
        assertFalse(PasswordUtil.verifyPassword("wrongPassword", hashedPassword));
    }

    @Test
    void testDifferentHashesForSamePassword() {
        String password = "testPassword";

        // Hash the same password twice
        String hash1 = PasswordUtil.hashPassword(password);
        String hash2 = PasswordUtil.hashPassword(password);

        // The hashes should be different due to different salts
        assertNotEquals(hash1, hash2);

        // Both should still verify correctly
        assertTrue(PasswordUtil.verifyPassword(password, hash1));
        assertTrue(PasswordUtil.verifyPassword(password, hash2));
    }

    @Test
    void testNullPasswordHandling() {
        // Test null password in verification
        assertFalse(PasswordUtil.verifyPassword(null, "$SHA256$salt$hash"));
        assertFalse(PasswordUtil.verifyPassword("password", null));
        assertFalse(PasswordUtil.verifyPassword(null, null));
    }

    @Test
    void testInvalidFormatHandling() {
        // Test invalid stored password formats
        assertFalse(PasswordUtil.verifyPassword("password", "plaintext"));
        assertFalse(PasswordUtil.verifyPassword("password", "$INVALID$salt$hash"));
        assertFalse(PasswordUtil.verifyPassword("password", "$SHA256$onlyOnepart"));
    }
}