package com.project.supplychain_blockchain.blockchain;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private final List<Block> chain = new ArrayList<>();

    public Blockchain() {
        chain.add(createGenesis());
    }

    private Block createGenesis() {
        return new Block(0, "Genesis Block", "0");
    }

    public synchronized Block addBlock(String data) {
        Block last = chain.get(chain.size() - 1);
        Block newBlock = new Block(chain.size(), data, last.getHash());
        chain.add(newBlock);
        return newBlock;
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block prev = chain.get(i - 1);
            if (!current.getHash().equals(current.calculateHash())) return false;
            if (!current.getPrevHash().equals(prev.getHash())) return false;
        }
        return true;
    }

    public List<Block> getChain() {
        return new ArrayList<>(chain);
    }
}
