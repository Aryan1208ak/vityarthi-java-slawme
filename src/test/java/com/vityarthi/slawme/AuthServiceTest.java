package com.vityarthi.slawme;

import com.vityarthi.slawme.exception.AuthenticationException;
import com.vityarthi.slawme.model.Role;
import com.vityarthi.slawme.model.User;
import com.vityarthi.slawme.service.AuthService;

/**
 * Unit Test Suite for AuthService & Security features.
 */
public class AuthServiceTest {

    public static void runAllTests() {
        System.out.println("Running AuthServiceTest suite...");
        testAdminLoginSuccess();
        testInvalidPasswordFailure();
        testRoleBasedAccessControl();
        System.out.println("✓ All AuthService Tests PASSED!");
    }

    private static void testAdminLoginSuccess() {
        AuthService auth = new AuthService();
        try {
            User user = auth.login("admin", "admin123");
            assert user != null : "User should not be null";
            assert user.getRole() == Role.ADMIN : "Role should be ADMIN";
        } catch (AuthenticationException e) {
            throw new RuntimeException("Test Failed: " + e.getMessage());
        }
    }

    private static void testInvalidPasswordFailure() {
        AuthService auth = new AuthService();
        try {
            auth.login("admin", "wrongpassword");
            assert false : "Login should have failed for wrong password";
        } catch (AuthenticationException e) {
            // Expected exception
        }
    }

    private static void testRoleBasedAccessControl() {
        AuthService auth = new AuthService();
        try {
            auth.login("auditor", "auditor123");
            try {
                auth.requireRole(Role.MANAGER);
                assert false : "Auditor should not be allowed to perform Manager actions";
            } catch (AuthenticationException e) {
                // Expected privilege denial
            }
        } catch (AuthenticationException e) {
            throw new RuntimeException("Test Failed: " + e.getMessage());
        }
    }
}
