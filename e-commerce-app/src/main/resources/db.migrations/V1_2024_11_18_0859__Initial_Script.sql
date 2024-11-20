CREATE TABLE IF NOT EXISTS user (
    user_id VARCHAR(36) PRIMARY KEY NOT NULL,
    first_name VARCHAR(150) NOT NULL,
    last_name VARCHAR(150),
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    gender VARCHAR(255) NOT NULL,
    user_type ENUM('ADMIN', 'USER') NOT NULL,
    address VARCHAR(255) NOT NULL,
    first_time_user BOOLEAN,
    password VARCHAR(20),
    otp_gen VARCHAR(6),
    otp_gen_time BIGINT,
    is_active BOOLEAN,
    created_by VARCHAR(36),
    created_date TIMESTAMP,
    updated_by VARCHAR(36),
    updated_date TIMESTAMP
);

CREATE TABLE category (
    category_id VARCHAR(36) PRIMARY KEY,
    category_name VARCHAR(150) NOT NULL,
    parent_category_id VARCHAR(36),
    is_active BOOLEAN,
    created_by VARCHAR(36),
    created_date TIMESTAMP,
    updated_by VARCHAR(36),
    updated_date TIMESTAMP
    FOREIGN KEY (parent_category_id) REFERENCES category(category_id)
);

CREATE TABLE product (
    product_id VARCHAR(36) PRIMARY KEY,
    product_name VARCHAR(150) NOT NULL,
    product_description VARCHAR(500),
    image_url VARCHAR(255),
    price DECIMAL(10, 2) NOT NULL,
    category_id VARCHAR(36),
    sub_category_id VARCHAR(36),
    is_active BOOLEAN,
    created_by VARCHAR(36),
    created_date TIMESTAMP,
    updated_by VARCHAR(36),
    updated_date TIMESTAMP
    FOREIGN KEY (category_id) REFERENCES category(category_id),
    FOREIGN KEY (sub_category_id) REFERENCES category(category_id)
);

CREATE TABLE inventory (
    inventory_id VARCHAR(36) PRIMARY KEY,
    product_id VARCHAR(36),
    available_items INT NOT NULL,
    lower_threshold INT NOT NULL,
    is_active BOOLEAN,
    created_by VARCHAR(36),
    created_date TIMESTAMP,
    updated_by VARCHAR(36),
    updated_date TIMESTAMP
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);