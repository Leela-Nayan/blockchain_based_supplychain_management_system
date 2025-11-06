package com.project.supplychain_blockchain.controller;

import com.project.supplychain_blockchain.model.Transaction;
import com.project.supplychain_blockchain.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/add")
    public String addTxn(@RequestBody Transaction t) {
        int id = transactionService.createTransaction(t);
        return "Transaction created: " + id;
    }

    @GetMapping("/history/{productId}")
    public List<Transaction> history(@PathVariable int productId) {
        return transactionService.getProductHistory(productId);
    }
}
