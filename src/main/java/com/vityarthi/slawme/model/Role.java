package com.vityarthi.slawme.model;

/**
 * Enum defining User Roles for Role-Based Access Control (RBAC).
 */
public enum Role {
    ADMIN("Administrator"),
    MANAGER("Inventory Manager"),
    AUDITOR("Compliance Auditor");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
