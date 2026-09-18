package com.vityarthi.slawme.model;

/**
 * User domain entity representing authenticated system user with password hashing.
 */
public class User implements Auditable {
    private final String username;
    private final String passwordHash;
    private final String salt;
    private final Role role;

    public User(String username, String passwordHash, String salt, Role role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getSalt() { return salt; }
    public Role getRole() { return role; }

    @Override
    public String getId() { return username; }

    @Override
    public String getAuditSummary() {
        return String.format("User[%s] Role: %s", username, role.getDisplayName());
    }
}
