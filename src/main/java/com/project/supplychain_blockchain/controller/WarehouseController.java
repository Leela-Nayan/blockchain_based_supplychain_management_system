package com.project.supplychain_blockchain.controller;

import com.project.supplychain_blockchain.dao.WarehouseDAO;
import com.project.supplychain_blockchain.model.Warehouse;
import com.project.supplychain_blockchain.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @PostMapping("/add")
    public String addWarehouse(@RequestBody Warehouse w) {
        warehouseService.addWarehouse(w);
        return "Warehouse added successfully!";
    }

    @GetMapping("/all")
    public List<Warehouse> getAll() {
        return warehouseService.getAllWarehouses();
    }

    @GetMapping("/{id}")
    public Warehouse getById(@PathVariable int id) {
        return warehouseService.getWarehouseById(id);
    }

}
