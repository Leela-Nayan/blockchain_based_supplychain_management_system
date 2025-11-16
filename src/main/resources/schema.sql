-- 1. USERS Table (Actors/Participants)
CREATE TABLE users (
                       user_id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       company_name VARCHAR(100) NOT NULL,
                       role VARCHAR(50) NOT NULL CHECK (role IN ('Admin', 'Manufacturer', 'Distributor', 'Retailer', 'Auditor')),
                       wallet_address VARCHAR(42) UNIQUE
);

-- 2. PRODUCTS Table (Master Product Data/SKUs)
CREATE TABLE products (
                          product_sku VARCHAR(50) PRIMARY KEY,
                          product_name VARCHAR(100) NOT NULL,
                          description TEXT,
                          unit_of_measure VARCHAR(10) NOT NULL
);

-- 3. FACILITIES Table (Locations)
CREATE TABLE facilities (
                            facility_id SERIAL PRIMARY KEY,
                            facility_name VARCHAR(100) NOT NULL,
                            address VARCHAR(255) NOT NULL,
                            facility_type VARCHAR(50) NOT NULL CHECK (facility_type IN ('Factory', 'Distribution Center', 'Retail Store', 'Lab')),
                            owner_id INTEGER REFERENCES users(user_id)
);

-- 4. SHIPMENT_ITEMS Table (Individual Tracked Assets)
CREATE TABLE shipment_items (
                                item_serial_number VARCHAR(100) PRIMARY KEY,
                                product_sku VARCHAR(50) NOT NULL REFERENCES products(product_sku),
                                origin_facility INTEGER REFERENCES facilities(facility_id) NOT NULL,
                                current_stage VARCHAR(50) NOT NULL, -- e.g., 'Manufactured', 'In Transit', 'Delivered'
                                manufacturer_id INTEGER REFERENCES users(user_id) NOT NULL,
                                creation_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                current_location INTEGER REFERENCES facilities(facility_id)
);

-- 5. CONTRACTS Table (1:1 Relationship Example)
CREATE TABLE contracts (
                           contract_id SERIAL PRIMARY KEY,
                           user_id INTEGER UNIQUE REFERENCES users(user_id) NOT NULL, -- UNIQUE ensures 1:1 relationship
                           contract_details TEXT,
                           effective_date DATE NOT NULL,
                           status VARCHAR(50) NOT NULL CHECK (status IN ('Active', 'Expired', 'Pending'))
);

-- 6. DOCUMENTS Table (External Records with Hashes)
CREATE TABLE documents (
                           document_id SERIAL PRIMARY KEY,
                           document_name VARCHAR(100) NOT NULL,
                           file_path VARCHAR(255) NOT NULL, -- Path to off-chain storage
                           document_hash VARCHAR(64) UNIQUE NOT NULL, -- Hash of file content, stored on the blockchain
                           uploaded_by_id INTEGER REFERENCES users(user_id) NOT NULL,
                           upload_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 7. AUDIT_CRITERIA Table (Normalization - Defining audit types)
CREATE TABLE audit_criteria (
                                criteria_id SERIAL PRIMARY KEY,
                                criteria_name VARCHAR(100) UNIQUE NOT NULL,
                                description TEXT,
                                required_role VARCHAR(50) -- e.g., 'Auditor'
);

-- 8. AUDITS Table (Compliance and Quality Checks - References AUDIT_CRITERIA)
CREATE TABLE audits (
                        audit_id SERIAL PRIMARY KEY,
                        item_serial_number VARCHAR(100) REFERENCES shipment_items(item_serial_number) NOT NULL,
                        criteria_id INTEGER REFERENCES audit_criteria(criteria_id) NOT NULL,
                        auditor_id INTEGER REFERENCES users(user_id) NOT NULL,
                        audit_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        result VARCHAR(50) NOT NULL CHECK (result IN ('Pass', 'Fail', 'Pending')),
                        notes TEXT,
                        data_hash VARCHAR(64) UNIQUE NOT NULL -- Hash of this audit record, stored on-chain
);

-- 9. ITEM_DOCUMENTS Table (M:N Link Table)
CREATE TABLE item_documents (
                                item_serial_number VARCHAR(100) REFERENCES shipment_items(item_serial_number) NOT NULL,
                                document_id INTEGER REFERENCES documents(document_id) NOT NULL,
                                PRIMARY KEY (item_serial_number, document_id),
                                upload_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 10. TRANSACTIONS Table (The Immutable Event Log & Blockchain Link)
CREATE TABLE transactions (
                              transaction_id SERIAL PRIMARY KEY,
                              item_serial_number VARCHAR(100) REFERENCES shipment_items(item_serial_number) NOT NULL,
                              actor_id INTEGER REFERENCES users(user_id) NOT NULL,
                              event_type VARCHAR(50) NOT NULL, -- e.g., 'Shipped', 'Received', 'Quality Checked'
                              event_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              from_location INTEGER REFERENCES facilities(facility_id),
                              to_location INTEGER REFERENCES facilities(facility_id),
                              data_hash VARCHAR(64) UNIQUE NOT NULL, -- **THE HASH FOR BLOCKCHAIN IMMUTABILITY**
                              transaction_data JSONB -- Flexible, non-critical transaction details
);
