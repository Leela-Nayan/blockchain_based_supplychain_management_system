package com.project.supplychain_blockchain.service;

import com.project.supplychain_blockchain.dao.ProductDAO;
import com.project.supplychain_blockchain.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    public void addProduct(Product p) { productDAO.addProduct(p); }
    public List<Product> getAllProducts() { return productDAO.getAllProducts(); }
    public Product getProductById(int id) { return productDAO.getProductById(id); }
}
