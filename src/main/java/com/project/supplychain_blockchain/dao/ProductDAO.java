package com.project.supplychain_blockchain.dao;

import com.project.supplychain_blockchain.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ProductDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addProduct(Product p) {
        String sql = "INSERT INTO products (name, description, manufacturer_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, p.getName(), p.getDescription(), p.getManufacturerId());
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class));
    }

    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Product.class));
    }
}
