package com.project.supplychain_blockchain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendShipmentUpdate(String to, String productName, String status) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Shipment Update for " + productName);
        msg.setText("Your shipment status has been updated to: " + status);
        mailSender.send(msg);
    }
}
