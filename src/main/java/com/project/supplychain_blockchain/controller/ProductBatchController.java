package com.project.supplychain_blockchain.controller;

import com.project.supplychain_blockchain.dao.ProductBatchDAO;
import com.project.supplychain_blockchain.model.ProductBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/batches")
public class ProductBatchController {

    @Autowired
    private ProductBatchDAO productBatchDAO;

    @PostMapping("/add")
    public String addBatch(@RequestBody ProductBatch b) {
        productBatchDAO.addBatch(b);
        return "Product batch added successfully!";
    }

    @GetMapping("/all")
    public List<ProductBatch> getAll() {
        return productBatchDAO.getAllBatches();
    }
}
