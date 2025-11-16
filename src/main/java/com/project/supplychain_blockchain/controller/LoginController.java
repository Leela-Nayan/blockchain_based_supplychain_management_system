package com.project.supplychain_blockchain.controller;

import com.project.supplychain_blockchain.model.LoginCredentials;
import com.project.supplychain_blockchain.service.LoginCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private LoginCredentialService credentialService;

    @PostMapping("/users/add")
    public ResponseEntity<?> registerUser(@RequestBody LoginCredentials user) {
        try {
            int newId = credentialService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("{\"userId\": " + newId + "}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginCredentials payload) {
        LoginCredentials user = credentialService.authenticate(payload.getEmail(), payload.getPassword());
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\": \"Invalid email or password\"}");
        return ResponseEntity.ok(user);
    }
}
