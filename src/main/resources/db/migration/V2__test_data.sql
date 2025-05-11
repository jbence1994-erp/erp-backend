INSERT INTO suppliers (name, phone, email)
VALUES ('Tech Solutions', NULL, 'info@techsolutions.hu'),
       ('Office Supplies Co.', '+36 30 765 4321', NULL),
       ('Industrial Parts Ltd.', NULL, 'contact@industrialparts.hu'),
       ('Houseware Inc.', '+36 70 987 6543', NULL),
       ('Audio Visual Experts', '+36 20 555 1212', 'support@avexperts.hu');

INSERT INTO products (name, serial_number, price, unit, description, supplier_id, on_stock, photo_file_name)
VALUES ('Wireless Mouse', 'WM-1001', 15.50, 'pcs', 'Ergonomic wireless mouse with USB receiver.', 1, 150, NULL),
       ('Mechanical Keyboard', 'KB-2002', 25.00, 'pcs', 'Mechanical keyboard with RGB backlight.', 1, 85, NULL),
       ('USB-C Cable', 'UC-3003', 5.99, 'pcs', '1 m USB-C to USB-C charging cable.', 2, 200, NULL),
       ('HDMI-VGA Adapter', 'HA-4004', 12.49, 'pcs', 'HDMI to VGA adapter, gold-plated connectors.', 2, 60, NULL),
       ('LED Monitor 24″', 'LM-5005', 129.99, 'pcs', '24-inch Full HD LED monitor with HDMI input.', 3, 30, NULL),
       ('External HDD 1 TB', 'HD-6006', 79.90, 'pcs', '1 TB external hard drive, USB 3.0.', 3, 45, NULL),
       ('Laptop Stand', 'LS-7007', 19.99, 'pcs', 'Adjustable aluminum laptop stand.', 4, 75, NULL),
       ('LED Desk Lamp', 'DL-8008', 22.50, 'pcs', 'LED desk lamp with adjustable brightness.', 4, 120, NULL),
       ('HD Webcam', 'WC-9009', 49.99, 'pcs', '1080p USB webcam with built-in mic.', 5, 65, NULL),
       ('Portable Speaker', 'PS-1010', 34.99, 'pcs', 'Bluetooth speaker with 10 h battery life.', 5, 90, NULL);

INSERT INTO users (first_name, last_name)
VALUES ('Alice', 'Smith'),
       ('Bob', 'Johnson'),
       ('Charlie', 'Williams'),
       ('Diana', 'Brown');

INSERT INTO profiles (email, password, photo_file_name, is_deleted, user_id)
VALUES ('alice.smith@example.com', '$2a$10$g1jri8EeEoYAZKze9yWdkuM06KzWxmEfqg0a5cA7I5jfew8ljHjhC', NULL, 0,
        (SELECT id FROM users WHERE first_name = 'Alice' AND last_name = 'Smith')),
       ('bob.johnson@example.com', '$2a$10$opMsK7wLet5T4Fj4/5Xe7uWAISjOCwTOPANVGToeyhdUgVSmKS4g.', NULL, 0,
        (SELECT id FROM users WHERE first_name = 'Bob' AND last_name = 'Johnson')),
       ('charlie.williams@example.com', '$2a$10$iiZeeCMgp.r9i.OBCNcimOZpUONG8wZZ2VoY0vt2rwhVnngiYHhK6', NULL, 0,
        (SELECT id FROM users WHERE first_name = 'Charlie' AND last_name = 'Williams')),
       ('diana.brown@example.com', '$2a$10$yNPCJfAa1Tso/vryjAWT/ex1Kkil4jZm4cAOcwStZyARA5Gy2Kpjm', NULL, 1,
        (SELECT id FROM users WHERE first_name = 'Diana' AND last_name = 'Brown'));
