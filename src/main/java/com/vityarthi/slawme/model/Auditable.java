package com.vityarthi.slawme.model;

/**
 * Interface representing auditable system components.
 */
public interface Auditable {
    String getAuditSummary();
    String getId();
}
