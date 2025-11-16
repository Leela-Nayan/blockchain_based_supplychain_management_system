package com.project.supplychain_blockchain.controller;

import com.project.supplychain_blockchain.dao.UserDAO;
import com.project.supplychain_blockchain.model.LoginCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/all")
    public List<LoginCredentials> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        int rows = userDAO.deleteUser(id);
        if (rows > 0)
            return ResponseEntity.ok("{\"status\":\"deleted\"}");
        return ResponseEntity.status(404).body("{\"error\":\"User not found\"}");
    }
}
