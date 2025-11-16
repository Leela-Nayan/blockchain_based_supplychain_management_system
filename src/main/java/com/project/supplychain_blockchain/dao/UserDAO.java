package com.project.supplychain_blockchain.dao;

import com.project.supplychain_blockchain.model.LoginCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<LoginCredentials> getAllUsers() {
        String sql = "SELECT user_id AS userId, name, email, password, role_id AS roleId FROM users ORDER BY user_id ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LoginCredentials.class));
    }

    public int deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }
}
