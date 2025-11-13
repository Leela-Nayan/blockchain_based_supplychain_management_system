package com.project.supplychain_blockchain.service;

import com.project.supplychain_blockchain.dao.WarehouseDAO;
import com.project.supplychain_blockchain.model.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseDAO warehouseDAO;

    public void addWarehouse(Warehouse w) {
        // Business logic placeholder (you can add validations if needed)
        warehouseDAO.addWarehouse(w);
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseDAO.getAllWarehouses();
    }

    public Warehouse getWarehouseById(int id) {
        return warehouseDAO.getWarehouseById(id);
    }
}
