package com.project.supplychain_blockchain.model;

public class Transaction {
    private Integer txnId;
    private Integer productId;
    private Integer senderId;
    private Integer receiverId;
    private String details;

    // Getters and Setters
    public Integer getTxnId() { return txnId; }
    public void setTxnId(Integer txnId) { this.txnId = txnId; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public Integer getSenderId() { return senderId; }
    public void setSenderId(Integer senderId) { this.senderId = senderId; }
    public Integer getReceiverId() { return receiverId; }
    public void setReceiverId(Integer receiverId) { this.receiverId = receiverId; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
