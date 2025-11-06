package com.project.supplychain_blockchain.service;

import com.project.supplychain_blockchain.dao.UserDAO;
import com.project.supplychain_blockchain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public void addUser(User user) { userDAO.addUser(user); }
    public List<User> getAllUsers() { return userDAO.getAllUsers(); }
    public User getUserById(int id) { return userDAO.getUserById(id); }
}
