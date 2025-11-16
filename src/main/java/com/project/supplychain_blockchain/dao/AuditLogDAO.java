package com.project.supplychain_blockchain.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AuditLogDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Insert audit log entry
    public int logEvent(String eventType, Integer userId, String details) {
        String sql = "INSERT INTO audit_log (event_type, user_id, details) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, eventType, userId, details);
    }

    // Return all audit logs
    public List<Map<String,Object>> findAll() {
        String sql = "SELECT log_id, event_type, user_id, timestamp, details FROM audit_log ORDER BY timestamp DESC";
        return jdbcTemplate.queryForList(sql);
    }
}
