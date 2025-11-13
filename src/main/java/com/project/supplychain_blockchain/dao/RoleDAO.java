package com.project.supplychain_blockchain.dao;

import com.project.supplychain_blockchain.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Role> getAllRoles() {
        String sql = "SELECT * FROM roles";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Role.class));
    }
}
