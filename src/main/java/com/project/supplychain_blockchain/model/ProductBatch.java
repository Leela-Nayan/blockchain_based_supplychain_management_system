package com.project.supplychain_blockchain.model;

import java.sql.Date;

public class ProductBatch {
    private int batchId;
    private int productId;
    private Date manufactureDate;
    private Date expiryDate;
    private int quantity;

    public int getBatchId() { return batchId; }
    public void setBatchId(int batchId) { this.batchId = batchId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public Date getManufactureDate() { return manufactureDate; }
    public void setManufactureDate(Date manufactureDate) { this.manufactureDate = manufactureDate; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
