package com.project.supplychain_blockchain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Total shipments per month
    @GetMapping("/shipments/monthly")
    public List<Map<String, Object>> getMonthlyShipments() {
        String sql = """
            SELECT TO_CHAR(delivery_date, 'YYYY-MM') AS month,
                   COUNT(shipment_id) AS total_shipments
            FROM shipments
            GROUP BY month
            ORDER BY month;
        """;
        return jdbcTemplate.queryForList(sql);
    }

    // Top 5 products by number of transactions
    @GetMapping("/products/top")
    public List<Map<String, Object>> getTopProducts() {
        String sql = """
            SELECT p.name, COUNT(t.txn_id) AS total_transactions
            FROM transactions t
            JOIN products p ON t.product_id = p.product_id
            GROUP BY p.name
            ORDER BY total_transactions DESC
            LIMIT 5;
        """;
        return jdbcTemplate.queryForList(sql);
    }

    // Warehouse capacity utilization
    @GetMapping("/warehouses/utilization")
    public List<Map<String, Object>> getWarehouseUtilization() {
        String sql = """
            SELECT w.location,
                   w.capacity,
                   COALESCE(SUM(b.quantity), 0) AS used_capacity,
                   ROUND((COALESCE(SUM(b.quantity), 0)::decimal / w.capacity) * 100, 2) AS utilization_percent
            FROM warehouses w
            LEFT JOIN shipments s ON w.warehouse_id = s.warehouse_id
            LEFT JOIN product_batch b ON s.batch_id = b.batch_id
            GROUP BY w.location, w.capacity
            ORDER BY utilization_percent DESC;
        """;
        return jdbcTemplate.queryForList(sql);
    }
}
