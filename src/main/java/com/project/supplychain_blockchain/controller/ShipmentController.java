package com.project.supplychain_blockchain.controller;

import com.project.supplychain_blockchain.dao.AuditLogDAO;
import com.project.supplychain_blockchain.dao.ShipmentDAO;
import com.project.supplychain_blockchain.dao.TransactionDAO;
import com.project.supplychain_blockchain.model.Shipment;
import com.project.supplychain_blockchain.model.Transaction;
import com.project.supplychain_blockchain.service.BlockchainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentDAO shipmentDAO;

    @Autowired
    private AuditLogDAO auditLogDAO;

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    // -------------------------------------------------------------
    // MANUFACTURER: CREATE SHIPMENT
    // -------------------------------------------------------------
    @PostMapping("/add")
    public ResponseEntity<?> addShipment(@RequestBody Shipment s) {

        shipmentDAO.addShipment(s);

        return ResponseEntity.ok("{\"message\":\"Shipment created\"}");
    }


    // -------------------------------------------------------------
    // MANUFACTURER / DISTRIBUTOR / RETAILER: VIEW ALL SHIPMENTS
    // -------------------------------------------------------------
    @GetMapping("/all")
    public List<Shipment> getAllShipments() {
        return shipmentDAO.getAllShipments();
    }


    // -------------------------------------------------------------
    // DISTRIBUTOR: MARK SHIPMENT RECEIVED
    // -------------------------------------------------------------
    @PatchMapping("/{id}/receive")
    public ResponseEntity<?> receiveShipment(
            @PathVariable("id") int id,
            @RequestBody(required = false) Map<String,Object> payload) {

        Integer userId = (payload != null && payload.get("userId") != null)
                ? ((Number) payload.get("userId")).intValue()
                : null;

        // Find batch_id for this shipment
        Integer batchId;
        try {
            batchId = jdbcTemplate.queryForObject(
                    "SELECT batch_id FROM shipments WHERE shipment_id = ?",
                    Integer.class,
                    id
            );
        } catch (Exception ex) {
            return ResponseEntity.status(404).body("{\"error\":\"Shipment not found\"}");
        }

        // Update status
        int rows = shipmentDAO.markReceived(id);
        if (rows <= 0)
            return ResponseEntity.status(404).body("{\"error\":\"Shipment not updated\"}");

        // Log audit
        String details = "Shipment " + id + " marked RECEIVED";
        auditLogDAO.logEvent("ShipmentReceived", userId, details);

        // Find product ID (for blockchain)
        Integer productId = null;
        try {
            productId = jdbcTemplate.queryForObject(
                    "SELECT pb.product_id FROM product_batch pb WHERE pb.batch_id = ?",
                    Integer.class,
                    batchId
            );
        } catch (Exception ignored) {}

        // Create transaction
        Transaction tx = new Transaction();
        tx.setProductId(productId);
        tx.setSenderId(null);
        tx.setReceiverId(userId);
        tx.setDetails(details);

        int txnId = transactionDAO.addTransaction(tx);

        // Add blockchain block
        if (txnId > 0) {
            blockchainService.addBlockForTransaction(txnId, details);
        }

        return ResponseEntity.ok("{\"result\":\"ok\"}");
    }


    // -------------------------------------------------------------
    // RETAILER: MARK SHIPMENT DELIVERED
    // -------------------------------------------------------------
    @PatchMapping("/{id}/deliver")
    public ResponseEntity<?> deliverShipment(
            @PathVariable("id") int id,
            @RequestBody(required = false) Map<String,Object> payload) {

        Integer userId = (payload != null && payload.get("userId") != null)
                ? ((Number) payload.get("userId")).intValue()
                : null;

        // Find batch_id
        Integer batchId;
        try {
            batchId = jdbcTemplate.queryForObject(
                    "SELECT batch_id FROM shipments WHERE shipment_id = ?",
                    Integer.class,
                    id
            );
        } catch (Exception ex) {
            return ResponseEntity.status(404).body("{\"error\":\"Shipment not found\"}");
        }

        // Update status
        int rows = shipmentDAO.markDelivered(id);
        if (rows <= 0)
            return ResponseEntity.status(404).body("{\"error\":\"Shipment not updated\"}");

        // Log audit
        String details = "Shipment " + id + " marked DELIVERED";
        auditLogDAO.logEvent("ShipmentDelivered", userId, details);

        // Find product ID
        Integer productId = null;
        try {
            productId = jdbcTemplate.queryForObject(
                    "SELECT pb.product_id FROM product_batch pb WHERE pb.batch_id = ?",
                    Integer.class,
                    batchId
            );
        } catch (Exception ignored) {}

        // Create transaction
        Transaction tx = new Transaction();
        tx.setProductId(productId);
        tx.setSenderId(null);
        tx.setReceiverId(userId);
        tx.setDetails(details);

        int txnId = transactionDAO.addTransaction(tx);

        // Add blockchain block
        if (txnId > 0) {
            blockchainService.addBlockForTransaction(txnId, details);
        }

        return ResponseEntity.ok("{\"result\":\"ok\"}");
    }

}
