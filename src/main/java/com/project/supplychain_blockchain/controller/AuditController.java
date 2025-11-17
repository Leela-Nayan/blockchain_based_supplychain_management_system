package com.project.supplychain_blockchain.controller;

import com.project.supplychain_blockchain.dao.AuditLogDAO;
import com.project.supplychain_blockchain.model.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditLogDAO auditLogDAO;

    // ---------------------------------------------------------
    // GET ALL LOGS (for auditor)
    // ---------------------------------------------------------
    @GetMapping("/all")
    public List<AuditLog> getAll() {
        return auditLogDAO.findAll();
    }

    // ---------------------------------------------------------
    // 🔥 GET ONLY REJECTED LOGS (for admin dashboard)
    // ---------------------------------------------------------
    @GetMapping("/rejected")
    public List<AuditLog> getRejected() {
        return auditLogDAO.findRejected();
    }

    // ---------------------------------------------------------
    // APPROVE SINGLE LOG
    // ---------------------------------------------------------
    @PostMapping("/approve")
    public ResponseEntity<?> approveOne(@RequestBody Map<String,Object> payload) {
        int logId = (int) payload.get("logId");
        int auditorId = (int) payload.get("auditorId");

        int rows = auditLogDAO.approveLog(logId, auditorId);

        return ResponseEntity.ok(Map.of("updated", rows));
    }

    // ---------------------------------------------------------
    // REJECT SINGLE LOG
    // ---------------------------------------------------------
    @PostMapping("/reject")
    public ResponseEntity<?> rejectOne(@RequestBody Map<String,Object> payload) {
        int logId = (int) payload.get("logId");
        int auditorId = (int) payload.get("auditorId");

        int rows = auditLogDAO.rejectLog(logId, auditorId);

        return ResponseEntity.ok(Map.of("updated", rows));
    }

    // ---------------------------------------------------------
    // BULK APPROVE
    // ---------------------------------------------------------
    @PostMapping("/approve/bulk")
    public ResponseEntity<?> bulkApprove(@RequestBody Map<String,Object> payload) {
        List<Integer> list = (List<Integer>) payload.get("logIds");
        int auditorId = (int) payload.get("auditorId");

        int[] logIds = list.stream().mapToInt(i -> i).toArray();

        int updated = auditLogDAO.bulkApprove(logIds, auditorId);

        return ResponseEntity.ok(Map.of("updated", updated));
    }

    // ---------------------------------------------------------
    // BULK REJECT
    // ---------------------------------------------------------
    @PostMapping("/reject/bulk")
    public ResponseEntity<?> bulkReject(@RequestBody Map<String,Object> payload) {
        List<Integer> list = (List<Integer>) payload.get("logIds");
        int auditorId = (int) payload.get("auditorId");

        int[] logIds = list.stream().mapToInt(i -> i).toArray();

        int updated = auditLogDAO.bulkReject(logIds, auditorId);

        return ResponseEntity.ok(Map.of("updated", updated));
    }
}
