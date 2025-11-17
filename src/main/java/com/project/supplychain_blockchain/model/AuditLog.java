package com.project.supplychain_blockchain.model;

import java.sql.Timestamp;

public class AuditLog {
    private Integer logId;
    private String eventType;
    private Integer userId;
    private Timestamp timestamp;
    private String details;

    // New fields
    private String status;            // PENDING / APPROVED / REJECTED
    private Integer auditedBy;        // user id of auditor
    private Timestamp auditDecisionTs;

    // Getters & setters
    public Integer getLogId() { return logId; }
    public void setLogId(Integer logId) { this.logId = logId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getAuditedBy() { return auditedBy; }
    public void setAuditedBy(Integer auditedBy) { this.auditedBy = auditedBy; }

    public Timestamp getAuditDecisionTs() { return auditDecisionTs; }
    public void setAuditDecisionTs(Timestamp auditDecisionTs) { this.auditDecisionTs = auditDecisionTs; }
}
