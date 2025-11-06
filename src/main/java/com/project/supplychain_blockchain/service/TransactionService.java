package com.project.supplychain_blockchain.service;

import com.project.supplychain_blockchain.dao.TransactionDAO;
import com.project.supplychain_blockchain.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private BlockchainService blockchainService;

    /**
     * Create a transaction (persist to DB) and add its fingerprint to the blockchain.
     * @param t Transaction model object
     * @return generated transaction id
     */
    public int createTransaction(Transaction t) {
        int txnId = transactionDAO.addTransaction(t);
        String data = String.format("txn:%d|product:%d|from:%d|to:%d|details:%s",
                txnId, t.getProductId(), t.getSenderId(), t.getReceiverId(), t.getDetails());
        blockchainService.addBlockForTransaction(txnId, data);
        return txnId;
    }

    public List<Transaction> getProductHistory(int productId) {
        return transactionDAO.getTransactionsForProduct(productId);
    }
}
