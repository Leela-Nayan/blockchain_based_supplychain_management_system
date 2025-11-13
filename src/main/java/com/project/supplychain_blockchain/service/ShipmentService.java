package com.project.supplychain_blockchain.service;

import com.project.supplychain_blockchain.dao.ShipmentDAO;
import com.project.supplychain_blockchain.model.Shipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentDAO shipmentDAO;
    private JdbcTemplate jdbcTemplate;
    private EmailService emailService;

    public void addShipment(Shipment s) {
        // Add logic later if you want to check warehouse capacity, etc.
        shipmentDAO.addShipment(s);
    }

    public List<Shipment> getAllShipments() {
        return shipmentDAO.getAllShipments();
    }

    public Shipment getShipmentById(int id) {
        return shipmentDAO.getShipmentById(id);
    }



    public void updateShipmentStatus(int shipmentId, String newStatus, String receiverEmail, String productName) {
        String sql = "UPDATE shipments SET status = ? WHERE shipment_id = ?";
        jdbcTemplate.update(sql, newStatus, shipmentId);
        emailService.sendShipmentUpdate(receiverEmail, productName, newStatus);
    }


}
