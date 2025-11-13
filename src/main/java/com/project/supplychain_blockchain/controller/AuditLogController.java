package com.project.supplychain_blockchain.controller;

import com.project.supplychain_blockchain.dao.AuditLogDAO;
import com.project.supplychain_blockchain.model.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    @Autowired
    private AuditLogDAO auditLogDAO;

    @GetMapping("/all")
    public List<AuditLog> getAll() {
        return auditLogDAO.getAllLogs();
    }
}
