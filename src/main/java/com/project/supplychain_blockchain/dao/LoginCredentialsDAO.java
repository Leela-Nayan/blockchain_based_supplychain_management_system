package com.project.supplychain_blockchain.dao;

import com.project.supplychain_blockchain.model.LoginCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LoginCredentialsDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addUser(LoginCredentials user) {
        String sql = "INSERT INTO users (name, email, password, role_id) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getPassword(),  // store raw password
                user.getRoleId());
    }

    public LoginCredentials findByEmail(String email) {
        String sql = "SELECT user_id AS userId, name, email, password, role_id AS roleId FROM users WHERE email = ?";
        List<LoginCredentials> list = jdbcTemplate.query(sql,
                new Object[]{email},
                new BeanPropertyRowMapper<>(LoginCredentials.class));

        return list.isEmpty() ? null : list.get(0);
    }
}
