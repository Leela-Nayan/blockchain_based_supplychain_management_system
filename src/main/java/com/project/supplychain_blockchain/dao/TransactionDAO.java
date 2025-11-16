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

@Repository
public class TransactionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Adds a new transaction and returns txn_id.
     */
    public int addTransaction(Transaction t) {

        // 🔥 FIX: explicitly return ONLY txn_id
        String sql = """
            INSERT INTO transactions (product_id, sender_id, receiver_id, details)
            VALUES (?, ?, ?, ?)
            RETURNING txn_id
        """;

        Integer txnId = jdbcTemplate.queryForObject(sql,
                Integer.class,
                t.getProductId(),
                t.getSenderId(),
                t.getReceiverId(),
                t.getDetails()
        );

        System.out.println("✅ Transaction inserted with txn_id=" + txnId);
        return txnId;
    }

    public List<Transaction> getTransactionsForProduct(int productId) {
        String sql = "SELECT * FROM transactions WHERE product_id = ? ORDER BY created_at";
        return jdbcTemplate.query(sql, new Object[]{productId}, new BeanPropertyRowMapper<>(Transaction.class));
    }

    public List<Transaction> getTransactions() {
        String sql = "SELECT * FROM transactions ORDER BY created_at";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transaction.class));
    }
}
