package com.project.supplychain_blockchain.dao;

import com.project.supplychain_blockchain.model.ProductBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductBatchDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addBatch(ProductBatch b) {
        String sql = "INSERT INTO product_batch (product_id, manufacture_date, expiry_date, quantity) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, b.getProductId(), b.getManufactureDate(), b.getExpiryDate(), b.getQuantity());
    }

    public List<ProductBatch> getAllBatches() {
        String sql = "SELECT * FROM product_batch";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProductBatch.class));
    }
}
