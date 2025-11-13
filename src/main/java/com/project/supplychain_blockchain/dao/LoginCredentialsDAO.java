package com.project.supplychain_blockchain.dao;

import com.project.supplychain_blockchain.model.LoginCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginCredentialsDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addCredentials(LoginCredentials lc) {
        String sql = "INSERT INTO login_credentials (user_id, username, password_hash) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, lc.getUserId(), lc.getUsername(), lc.getPasswordHash());
    }

    public LoginCredentials findByUsername(String username) {
        String sql = "SELECT * FROM login_credentials WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, new BeanPropertyRowMapper<>(LoginCredentials.class));
    }
}
