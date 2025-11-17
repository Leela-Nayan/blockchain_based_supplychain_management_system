package com.project.supplychain_blockchain.dao;

import com.project.supplychain_blockchain.model.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuditLogDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    // -------------------------------------------------------
    // NEW: Add audit log entry (used everywhere in backend)
    // -------------------------------------------------------
    public void logEvent(String eventType, Integer userId, String details) {
        String sql = """
            INSERT INTO audit_log (event_type, user_id, details, status)
            VALUES (?, ?, ?, 'PENDING')
        """;

        jdbcTemplate.update(sql, eventType, userId, details);
    }


    // -------------------------------------------------------
    // FETCH ALL LOGS
    // -------------------------------------------------------
    public List<AuditLog> findAll() {
        String sql = """
            SELECT 
                log_id AS logId,
                event_type AS eventType,
                user_id AS userId,
                timestamp,
                details,
                status,
                audited_by AS auditedBy,
                audit_decision_ts AS auditDecisionTs
            FROM audit_log
            ORDER BY timestamp DESC
        """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AuditLog.class));
    }


    // -------------------------------------------------------
    // FETCH REJECTED LOGS (Admin alert)
    // -------------------------------------------------------
    public List<AuditLog> findRejected() {
        String sql = """
            SELECT 
                log_id AS logId,
                event_type AS eventType,
                user_id AS userId,
                timestamp,
                details,
                status,
                audited_by AS auditedBy,
                audit_decision_ts AS auditDecisionTs
            FROM audit_log
            WHERE status = 'REJECTED'
            ORDER BY audit_decision_ts DESC
        """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AuditLog.class));
    }


    // -------------------------------------------------------
    // APPROVE ONE LOG
    // -------------------------------------------------------
    public int approveLog(int logId, int auditorId) {
        String sql = """
            UPDATE audit_log
            SET status='APPROVED', audited_by=?, audit_decision_ts=CURRENT_TIMESTAMP
            WHERE log_id=?
        """;
        return jdbcTemplate.update(sql, auditorId, logId);
    }


    // -------------------------------------------------------
    // REJECT ONE LOG
    // -------------------------------------------------------
    public int rejectLog(int logId, int auditorId) {
        String sql = """
            UPDATE audit_log
            SET status='REJECTED', audited_by=?, audit_decision_ts=CURRENT_TIMESTAMP
            WHERE log_id=?
        """;
        return jdbcTemplate.update(sql, auditorId, logId);
    }


    // -------------------------------------------------------
    // BULK APPROVE
    // -------------------------------------------------------
    public int bulkApprove(int[] logIds, int auditorId) {
        int total = 0;
        for (int id : logIds) {
            total += approveLog(id, auditorId);
        }
        return total;
    }


    // -------------------------------------------------------
    // BULK REJECT
    // -------------------------------------------------------
    public int bulkReject(int[] logIds, int auditorId) {
        int total = 0;
        for (int id : logIds) {
            total += rejectLog(id, auditorId);
        }
        return total;
    }

}
