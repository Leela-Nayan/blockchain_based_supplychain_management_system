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

    public List<AuditLog> getAllLogs() {
        String sql = "SELECT * FROM audit_log ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AuditLog.class));
    }
}
