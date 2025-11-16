package com.project.supplychain_blockchain.service;

import com.project.supplychain_blockchain.dao.LoginCredentialsDAO;
import com.project.supplychain_blockchain.model.LoginCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginCredentialService {

    @Autowired
    private LoginCredentialsDAO loginCredentialsDAO;

    public int registerUser(LoginCredentials user) {
        if (user.getEmail() == null || user.getEmail().isBlank())
            throw new IllegalArgumentException("Email is required");

        if (user.getPassword() == null || user.getPassword().isBlank())
            throw new IllegalArgumentException("Password is required");

        LoginCredentials existing = loginCredentialsDAO.findByEmail(user.getEmail());
        if (existing != null)
            throw new IllegalArgumentException("Email already registered");

        // Store plain password
        return loginCredentialsDAO.addUser(user);
    }

    public LoginCredentials authenticate(String email, String rawPassword) {

        if (email == null || rawPassword == null) {
            return null;
        }

        // Get user from DB
        LoginCredentials user = loginCredentialsDAO.findByEmail(email);
        if (user == null) {
            return null;
        }

        // Compare raw passwords directly
        if (!rawPassword.equals(user.getPassword())) {
            return null;
        }

        // Remove password before returning
        user.setPassword(null);
        return user;
    }
}
