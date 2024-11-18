CREATE TABLE user (
    user_id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(150),
    last_name VARCHAR(150),
    email VARCHAR(100),
    phone_number VARCHAR(20),
    gender VARCHAR(255),
    user_type ENUM('ADMIN', 'USER'),
    address VARCHAR(255),
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
