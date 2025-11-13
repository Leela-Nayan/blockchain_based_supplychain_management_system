package com.project.supplychain_blockchain.controller;

import com.project.supplychain_blockchain.dao.ShipmentDAO;
import com.project.supplychain_blockchain.model.Shipment;
import com.project.supplychain_blockchain.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @PostMapping("/add")
    public String addShipment(@RequestBody Shipment s) {
        shipmentService.addShipment(s);
        return "Shipment added successfully!";
    }

    @GetMapping("/all")
    public List<Shipment> getAll() {
        return shipmentService.getAllShipments();
    }

    @GetMapping("/{id}")
    public Shipment getById(@PathVariable int id) {
        return shipmentService.getShipmentById(id);
    }

}
