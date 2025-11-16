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

    public Block addBlockForTransaction(int txnId, String data) {

        System.out.println("---- BLOCKCHAIN DEBUG ----");
        System.out.println("Creating block for txnId: " + txnId);
        System.out.println("Data: " + data);

        Block block = blockchain.addBlock(data);

        System.out.println("Block created:");
        System.out.println("  index = " + block.getIndex());
        System.out.println("  hash  = " + block.getHash());
        System.out.println("  prev  = " + block.getPrevHash());

        String sql = "INSERT INTO blockchain (txn_id, block_index, hash, prev_hash, data) VALUES (?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql,
                    txnId,
                    block.getIndex(),
                    block.getHash(),
                    block.getPrevHash(),
                    block.getData()
            );

            System.out.println("BLOCKCHAIN INSERT SUCCESS!");

        } catch (Exception e) {
            System.out.println("❌ BLOCKCHAIN INSERT FAILED ❌");
            e.printStackTrace();
        }

        return block;
    }

    public boolean isValid() {
        return blockchain.isChainValid();
    }
}
