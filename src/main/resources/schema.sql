-- USERS TABLE
CREATE TABLE IF NOT EXISTS users (
                                     user_id SERIAL PRIMARY KEY,
                                     name VARCHAR(100) NOT NULL,
                                     email VARCHAR(150) UNIQUE NOT NULL,
                                     role_id INT
);

-- ROLES TABLE
CREATE TABLE IF NOT EXISTS roles (
                                     role_id SERIAL PRIMARY KEY,
                                     role_name VARCHAR(50) NOT NULL UNIQUE
);

-- PRODUCTS TABLE
CREATE TABLE IF NOT EXISTS products (
                                        product_id SERIAL PRIMARY KEY,
                                        name VARCHAR(150) NOT NULL,
                                        description TEXT,
                                        manufacturer_id INT REFERENCES users(user_id)
);

-- PRODUCT BATCH TABLE
CREATE TABLE IF NOT EXISTS product_batch (
                                             batch_id SERIAL PRIMARY KEY,
                                             product_id INT REFERENCES products(product_id),
                                             manufacture_date DATE NOT NULL,
                                             expiry_date DATE,
                                             quantity INT NOT NULL
);

-- WAREHOUSE TABLE
CREATE TABLE IF NOT EXISTS warehouses (
                                          warehouse_id SERIAL PRIMARY KEY,
                                          location VARCHAR(150) NOT NULL,
                                          capacity INT NOT NULL
);

-- SHIPMENTS TABLE
CREATE TABLE IF NOT EXISTS shipments (
                                         shipment_id SERIAL PRIMARY KEY,
                                         batch_id INT REFERENCES product_batch(batch_id),
                                         warehouse_id INT REFERENCES warehouses(warehouse_id),
                                         dispatch_date DATE NOT NULL,
                                         delivery_date DATE,
                                         status VARCHAR(50) NOT NULL
);

-- TRANSACTIONS TABLE
CREATE TABLE IF NOT EXISTS transactions (
                                            txn_id SERIAL PRIMARY KEY,
                                            product_id INT REFERENCES products(product_id),
                                            sender_id INT REFERENCES users(user_id),
                                            receiver_id INT REFERENCES users(user_id),
                                            details TEXT,
                                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- BLOCKCHAIN TABLE (metadata for each transaction)
CREATE TABLE IF NOT EXISTS blockchain (
                                          block_id SERIAL PRIMARY KEY,
                                          txn_id INT REFERENCES transactions(txn_id),
                                          block_index INT NOT NULL,
                                          hash VARCHAR(128) NOT NULL,
                                          prev_hash VARCHAR(128) NOT NULL,
                                          data TEXT,
                                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
