package com.vityarthi.slawme.service;

import com.vityarthi.slawme.exception.AuthenticationException;
import com.vityarthi.slawme.model.Role;
import com.vityarthi.slawme.model.User;
import com.vityarthi.slawme.util.AuditLogger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Authentication and Session Security Service.
 */
public class AuthService {
    private final Map<String, User> userDatabase = new ConcurrentHashMap<>();
    private User currentUser = null;

    public AuthService() {
        // Seed initial admin, manager, auditor users
        registerUser("admin", "admin123", Role.ADMIN);
        registerUser("manager", "manager123", Role.MANAGER);
        registerUser("auditor", "auditor123", Role.AUDITOR);
    }

    public synchronized void registerUser(String username, String rawPassword, Role role) {
        String salt = generateSalt();
        String hash = hashPassword(rawPassword, salt);
        User user = new User(username, hash, salt, role);
        userDatabase.put(username.toLowerCase(), user);
        AuditLogger.getInstance().log("INFO", "SYSTEM", "Registered new user: " + user.getAuditSummary());
    }

    public synchronized User login(String username, String rawPassword) throws AuthenticationException {
        User user = userDatabase.get(username.toLowerCase());
        if (user == null) {
            AuditLogger.getInstance().log("WARN", username, "Failed login attempt: User not found");
            throw new AuthenticationException("Invalid username or password");
        }

        String computedHash = hashPassword(rawPassword, user.getSalt());
        if (!computedHash.equals(user.getPasswordHash())) {
            AuditLogger.getInstance().log("WARN", username, "Failed login attempt: Invalid password");
            throw new AuthenticationException("Invalid username or password");
        }

        this.currentUser = user;
        AuditLogger.getInstance().log("INFO", username, "Successful login as " + user.getRole().getDisplayName());
        return user;
    }

    public synchronized void logout() {
        if (currentUser != null) {
            AuditLogger.getInstance().log("INFO", currentUser.getUsername(), "Logged out");
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public void requireRole(Role minRole) throws AuthenticationException {
        if (!isAuthenticated()) {
            throw new AuthenticationException("Operation requires active authentication. Please login.");
        }
        if (currentUser.getRole() == Role.AUDITOR && minRole != Role.AUDITOR) {
            throw new AuthenticationException("Permission Denied: Auditors have read-only privileges.");
        }
        if (currentUser.getRole() == Role.MANAGER && minRole == Role.ADMIN) {
            throw new AuthenticationException("Permission Denied: Operation requires Administrator privileges.");
        }
    }

    public static String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 Hashing Algorithm Not Found", e);
        }
    }
}
