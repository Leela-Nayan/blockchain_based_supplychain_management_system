package com.project.supplychain_blockchain.dao;

import com.project.supplychain_blockchain.model.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WarehouseDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addWarehouse(Warehouse w) {
        String sql = "INSERT INTO warehouses (location, capacity) VALUES (?, ?)";
        jdbcTemplate.update(sql, w.getLocation(), w.getCapacity());
    }

    public List<Warehouse> getAllWarehouses() {
        String sql = "SELECT * FROM warehouses";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Warehouse.class));
    }

    public Warehouse getWarehouseById(int id) {
        String sql = "SELECT * FROM warehouses WHERE warehouse_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Warehouse.class));
    }
}
