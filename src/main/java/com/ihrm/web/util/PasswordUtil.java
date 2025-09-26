package com.ihrm.web.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {
    private static final String ALGORITHM = "SHA256";
    private static final String FORMAT_PREFIX = "$" + ALGORITHM + "$";
    private static final int SALT_LENGTH = 16;

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password) {
        String salt = generateSalt();
        String hash = computeHash(password, salt);
        return FORMAT_PREFIX + salt + "$" + hash;
    }

    public static boolean verifyPassword(String password, String storedPassword) {
        if (password == null || storedPassword == null) {
            return false;
        }

        if (!storedPassword.startsWith(FORMAT_PREFIX)) {
            return false;
        }

        String[] parts = storedPassword.substring(FORMAT_PREFIX.length()).split("\\$");
        if (parts.length != 2) {
            return false;
        }

        String salt = parts[0];
        String storedHash = parts[1];
        String computedHash = computeHash(password, salt);

        return constantTimeEquals(storedHash, computedHash);
    }

    private static String computeHash(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String saltedPassword = salt + password;
            byte[] hashedBytes = md.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.length() != b.length()) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }

    public static void main(String[] args) {
        String password = "testPassword123";
        String hashed = hashPassword(password);
        System.out.println("Original password: " + password);
        System.out.println("Hashed password: " + hashed);
        System.out.println("Verification result: " + verifyPassword(password, hashed));
        System.out.println("Wrong password verification: " + verifyPassword("wrongPassword", hashed));
    }
}