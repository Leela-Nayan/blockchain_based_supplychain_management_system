package com.project.supplychain_blockchain.service;

import com.project.supplychain_blockchain.dao.LoginCredentialsDAO;
import com.project.supplychain_blockchain.model.LoginCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginCredentialService {

    @Autowired
    private LoginCredentialsDAO loginCredentialsDAO;

    public void registerCredentials(LoginCredentials lc) {
        // You can later add password hashing logic here
        loginCredentialsDAO.addCredentials(lc);
    }

    public LoginCredentials getByUsername(String username) {
        return loginCredentialsDAO.findByUsername(username);
    }

    public boolean validateLogin(String username, String passwordHash) {
        try {
            LoginCredentials lc = loginCredentialsDAO.findByUsername(username);
            return lc.getPasswordHash().equals(passwordHash);
        } catch (Exception e) {
            return false;
        }
    }
}
