package com.project.supplychain_blockchain.dao;

import com.project.supplychain_blockchain.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Adds a new transaction record to the database and returns its generated txn_id.
     */
    public int addTransaction(Transaction t) {
        String sql = "INSERT INTO transactions (product_id, sender_id, receiver_id, details) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, t.getProductId());
            ps.setInt(2, t.getSenderId());
            ps.setInt(3, t.getReceiverId());
            ps.setString(4, t.getDetails());
            return ps;
        }, keyHolder);

        // ✅ FIX: PostgreSQL returns multiple columns as keys, so use getKeys() instead of getKey()
        Map<String, Object> keys = keyHolder.getKeys();
        int txnId = -1;

        if (keys != null && keys.containsKey("txn_id")) {
            txnId = ((Number) keys.get("txn_id")).intValue();
        }

        // 🔍 Log inserted transaction info for debugging/demo
        System.out.println("✅ Transaction inserted successfully: " + t.getDetails());
        if (txnId != -1) {
            System.out.println("   → Generated txn_id = " + txnId);
        } else {
            System.out.println("⚠️ Warning: Could not fetch generated txn_id (driver returned multiple keys).");
        }

        return txnId;
    }

    /**
     * Retrieves all transactions for a given product in chronological order.
     */
    public List<Transaction> getTransactionsForProduct(int productId) {
        String sql = "SELECT * FROM transactions WHERE product_id = ? ORDER BY created_at";
        return jdbcTemplate.query(sql, new Object[]{productId}, new BeanPropertyRowMapper<>(Transaction.class));
    }

    public List<Transaction> getTransactions() {
        String sql = "SELECT * FROM transactions ORDER BY created_at";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transaction.class));
    }

}
