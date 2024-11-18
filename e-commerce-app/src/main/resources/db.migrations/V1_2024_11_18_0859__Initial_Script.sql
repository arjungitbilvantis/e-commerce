Got it! Here's the updated SQL statement with the `IF NOT EXISTS` clause:

```sql
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