package com.project.supplychain_blockchain.controller;

import com.project.supplychain_blockchain.model.LoginCredentials;
import com.project.supplychain_blockchain.service.LoginCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginCredentialService loginService;

    @PostMapping("/register")
    public String register(@RequestBody LoginCredentials lc) {
        loginService.registerCredentials(lc);
        return "User credentials registered!";
    }

    @PostMapping("/validate")
    public String validate(@RequestBody LoginCredentials lc) {
        boolean valid = loginService.validateLogin(lc.getUsername(), lc.getPasswordHash());
        return valid ? "Login successful!" : "Invalid credentials!";
    }
}
