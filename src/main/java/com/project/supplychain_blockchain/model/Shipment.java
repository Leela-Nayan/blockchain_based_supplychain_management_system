package com.project.supplychain_blockchain.model;

import java.sql.Date;

public class Shipment {
    private int shipmentId;
    private int batchId;
    private int warehouseId;
    private Date dispatchDate;
    private Date deliveryDate;
    private String status;

    public int getShipmentId() { return shipmentId; }
    public void setShipmentId(int shipmentId) { this.shipmentId = shipmentId; }
    public int getBatchId() { return batchId; }
    public void setBatchId(int batchId) { this.batchId = batchId; }
    public int getWarehouseId() { return warehouseId; }
    public void setWarehouseId(int warehouseId) { this.warehouseId = warehouseId; }
    public Date getDispatchDate() { return dispatchDate; }
    public void setDispatchDate(Date dispatchDate) { this.dispatchDate = dispatchDate; }
    public Date getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(Date deliveryDate) { this.deliveryDate = deliveryDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
