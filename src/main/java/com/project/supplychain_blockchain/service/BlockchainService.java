package com.project.supplychain_blockchain.service;

import com.project.supplychain_blockchain.blockchain.Block;
import com.project.supplychain_blockchain.blockchain.Blockchain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BlockchainService {

    private final Blockchain blockchain = new Blockchain();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Create a block for a transaction and persist block metadata in DB.
     * @param txnId the transaction id this block represents
     * @param data human-readable transaction fingerprint
     * @return created Block
     */
    public Block addBlockForTransaction(int txnId, String data) {
        Block block = blockchain.addBlock(data);
        String sql = "INSERT INTO blockchain (txn_id, block_index, hash, prev_hash, data) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, txnId, block.getIndex(), block.getHash(), block.getPrevHash(), block.getData());
        return block;
    }

    public boolean isValid() {
        return blockchain.isChainValid();
    }
}
