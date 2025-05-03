CREATE DATABASE IF NOT EXISTS erp
    DEFAULT CHARACTER SET utf8 COLLATE utf8_hungarian_ci;

USE erp;

CREATE TABLE IF NOT EXISTS suppliers
(
    id    BIGINT(20)   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    phone VARCHAR(25),
    email VARCHAR(75)
);

CREATE TABLE IF NOT EXISTS products
(
    id              BIGINT(20)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255)   NOT NULL,
    serial_number   VARCHAR(25),
    price           DECIMAL(10, 2) NOT NULL,
    unit            VARCHAR(255)   NOT NULL,
    description     TEXT,
    supplier_id     BIGINT         NOT NULL,
    on_stock        INT            NOT NULL,
    photo_file_name VARCHAR(41),
    CONSTRAINT fk_supplier
        FOREIGN KEY (supplier_id) REFERENCES suppliers (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);
