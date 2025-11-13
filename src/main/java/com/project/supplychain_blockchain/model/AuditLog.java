package com.project.supplychain_blockchain.model;

import java.sql.Timestamp;

public class AuditLog {
    private int logId;
    private String eventType;
    private int userId;
    private Timestamp timestamp;
    private String details;

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
