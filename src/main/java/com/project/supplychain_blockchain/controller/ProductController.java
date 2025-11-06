package com.project.supplychain_blockchain.controller;

import com.project.supplychain_blockchain.model.Product;
import com.project.supplychain_blockchain.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public String addProduct(@RequestBody Product p) {
        productService.addProduct(p);
        return "Product added";
    }

    @GetMapping("/all")
    public List<Product> allProducts() { return productService.getAllProducts(); }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable int id) { return productService.getProductById(id); }
}
