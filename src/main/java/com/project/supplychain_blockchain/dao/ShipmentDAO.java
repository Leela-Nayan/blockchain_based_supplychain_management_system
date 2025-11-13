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

    public void addShipment(Shipment s) {
        String sql = "INSERT INTO shipments (batch_id, warehouse_id, dispatch_date, delivery_date, status) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, s.getBatchId(), s.getWarehouseId(), s.getDispatchDate(), s.getDeliveryDate(), s.getStatus());
    }

    public List<Shipment> getAllShipments() {
        String sql = "SELECT * FROM shipments";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Shipment.class));
    }

    public Shipment getShipmentById(int id) {
        String sql = "SELECT * FROM shipments WHERE shipment_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Shipment.class));
    }
}
