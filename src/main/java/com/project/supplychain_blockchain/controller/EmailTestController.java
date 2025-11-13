package com.project.supplychain_blockchain.controller;

import com.project.supplychain_blockchain.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/test")
    public String sendTestEmail(@RequestParam String to) {
        emailService.sendShipmentUpdate(to, "Test Product", "Delivered");
        return "Email sent to " + to;
    }
}
