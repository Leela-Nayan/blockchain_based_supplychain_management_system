package com.project.supplychain_blockchain.blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;

public class Block {
    private int index;
    private String timestamp;
    private String data;
    private String prevHash;
    private String hash;

    public Block(int index, String data, String prevHash) {
        this.index = index;
        this.timestamp = LocalDateTime.now().toString();
        this.data = data;
        this.prevHash = prevHash;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        try {
            String input = index + timestamp + data + prevHash;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // getters
    public int getIndex() { return index; }
    public String getTimestamp() { return timestamp; }
    public String getData() { return data; }
    public String getPrevHash() { return prevHash; }
    public String getHash() { return hash; }
}
