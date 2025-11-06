-- Users table
CREATE TABLE IF NOT EXISTS users (
                                     user_id SERIAL PRIMARY KEY,
                                     name VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL,
    email VARCHAR(150) UNIQUE
    );

-- Products table
CREATE TABLE IF NOT EXISTS products (
                                        product_id SERIAL PRIMARY KEY,
                                        name VARCHAR(150) NOT NULL,
    description TEXT,
    manufacturer_id INT REFERENCES users(user_id)
    );

-- Transactions table (off-chain ledger of transfers)
CREATE TABLE IF NOT EXISTS transactions (
                                            txn_id SERIAL PRIMARY KEY,
                                            product_id INT REFERENCES products(product_id),
    sender_id INT REFERENCES users(user_id),
    receiver_id INT REFERENCES users(user_id),
    details TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Blockchain table (stores block metadata and hash chain)
CREATE TABLE IF NOT EXISTS blockchain (
                                          block_id SERIAL PRIMARY KEY,
                                          txn_id INT REFERENCES transactions(txn_id),
    block_index INT NOT NULL,
    hash VARCHAR(128) NOT NULL,
    prev_hash VARCHAR(128) NOT NULL,
    data TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
