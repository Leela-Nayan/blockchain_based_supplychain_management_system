package com.project.supplychain_blockchain.controller;

import com.project.supplychain_blockchain.dao.AuditLogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditLogDAO auditLogDAO;

    // Admin / Auditor: list audit logs
    @GetMapping("/all")
    public List<Map<String,Object>> getAll() {
        return auditLogDAO.findAll();
    }
}
