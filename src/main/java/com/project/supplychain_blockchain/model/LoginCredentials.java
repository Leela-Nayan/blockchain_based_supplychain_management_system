package com.project.supplychain_blockchain.model;

public class LoginCredentials {
    private int loginId;
    private int userId;
    private String username;
    private String passwordHash;

    public int getLoginId() { return loginId; }
    public void setLoginId(int loginId) { this.loginId = loginId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
