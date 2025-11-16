package com.project.supplychain_blockchain.dao;

import com.project.supplychain_blockchain.model.Shipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShipmentDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addShipment(Shipment shipment) {
        String sql = "INSERT INTO shipments (batch_id, warehouse_id, dispatch_date, delivery_date, status) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                shipment.getBatchId(),
                shipment.getWarehouseId(),
                shipment.getDispatchDate(),
                shipment.getDeliveryDate(),
                shipment.getStatus());
    }

    public List<Shipment> getAllShipments() {
        String sql = "SELECT * FROM shipments ORDER BY shipment_id ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Shipment.class));
    }

    public Shipment getShipmentById(int id) {
        String sql = "SELECT * FROM shipments WHERE shipment_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Shipment.class), id);
    }

    public int markReceived(int shipmentId) {
        String sql = "UPDATE shipments SET status = ?, delivery_date = CURRENT_DATE WHERE shipment_id = ?";
        return jdbcTemplate.update(sql, "Received", shipmentId);
    }

    public int markDelivered(int shipmentId) {
        String sql = "UPDATE shipments SET status = ?, delivery_date = CURRENT_DATE WHERE shipment_id = ?";
        return jdbcTemplate.update(sql, "Delivered", shipmentId);
    }

    public String getStatus(int shipmentId) {
        try {
            String sql = "SELECT status FROM shipments WHERE shipment_id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{shipmentId}, String.class);
        } catch (Exception e) {
            return null;
        }
    }
}
