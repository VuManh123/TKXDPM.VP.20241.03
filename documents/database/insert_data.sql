INSERT INTO USER (name, email, address, phone, user_type, password, status)
VALUES
('Alice Johnson', 'alice@example.com', '123 Maple Street', '555-1234', 1, 'password1', 1),
('Bob Smith', 'bob@example.com', '456 Oak Avenue', '555-5678', 2, 'password2', 1),
('Carol Williams', 'carol@example.com', '789 Pine Road', '555-8765', 1, 'password3', 1),
('David Brown', 'david@example.com', '101 Birch Lane', '555-4321', 2, 'password4', 0),
('Eve Davis', 'eve@example.com', '202 Cedar Street', '555-6789', 1, 'password5', 1),
('Frank Miller', 'frank@example.com', '303 Spruce Drive', '555-9876', 2, 'password6', 0),
('Grace Wilson', 'grace@example.com', '404 Redwood Road', '555-6543', 1, 'password7', 1),
('Hank Moore', 'hank@example.com', '505 Elm Street', '555-3210', 2, 'password8', 0),
('Ivy Taylor', 'ivy@example.com', '606 Maple Avenue', '555-2109', 1, 'password9', 1),
('Jack Anderson', 'jack@example.com', '707 Pine Street', '555-0198', 2, 'password10', 0);

-- Insert data into MEDIA table
INSERT INTO MEDIA (type, category, price, quantity, title, value, imageUrl, support_for_rush_delivery)
VALUES
    ('Book', 'Fiction', 19, 10, 'The Great Gatsby', 100, 'url1', 1),
    ('Book', 'Non-Fiction', 24, 15, 'Sapiens', 200, 'url2', 0),
    ('DVD', 'Movie', 14, 20, 'Inception', 150, 'url3', 1),
    ('CD', 'Music', 9, 25, 'Thriller', 120, 'url4', 0),
    ('CD', 'Vinyl', 29, 5, 'Abbey Road', 250, 'url5', 1),
    ('Book', 'Fiction', 18, 8, '1984', 180, 'url6', 0),
    ('DVD', 'Series', 39, 12, 'Game of Thrones Season 1', 400, 'url7', 1),
    ('CD', 'Music', 11, 18, 'Back in Black', 130, 'url8', 0),
    ('Book', 'Vinyl', 25, 7, 'The Wall', 300, 'url9', 1),
    ('Book', 'Non-Fiction', 22, 10, 'Educated', 220, 'url10', 0);

-- Insert data into CD table
INSERT INTO CD (id, artist, recordLabel, musicType, releasedDate)
VALUES
    (4, 'Michael Jackson', 'Epic Records', 'Pop', '1982-11-30'),
    (5, 'The Beatles', 'Apple Records', 'Rock', '1969-09-26'),
    (8, 'AC/DC', 'Albert Productions', 'Rock', '1980-07-25');

-- Insert data into BOOKS table
INSERT INTO BOOKS (id, author, coverType, publisher, publishDate, numOfPages, language, bookCategory)
VALUES
    (1, 'F. Scott Fitzgerald', 'Hardcover', 'Scribner', '1925-04-10', 218, 'English', 'Classic'),
    (2, 'Yuval Noah Harari', 'Paperback', 'Harper', '2014-09-04', 512, 'English', 'History'),
    (6, 'George Orwell', 'Paperback', 'Secker & Warburg', '1949-06-08', 328, 'English', 'Dystopian'),
    (10, 'Tara Westover', 'Hardcover', 'Random House', '2018-02-20', 352, 'English', 'Memoir');

-- Insert data into DVD table
INSERT INTO DVD (id, discType, director, runtime, studio, subtitle, language, releasedDate, filmType)
VALUES
    (3, 'Blu-ray', 'Christopher Nolan', 148, 'Warner Bros.', 'English', 'English', '2010-07-16', 'Science Fiction'),
    (7, 'DVD', 'Various', 600, 'HBO', 'English', 'English', '2011-04-17', 'Fantasy');

-- Insert data into CART table
INSERT INTO CART (userID)
VALUES
    (1),
    (2);

-- Insert data into CART_MEDIA table
INSERT INTO CART_MEDIA (cartID, mediaID, number_of_products)
VALUES
    (1, 1, 2),
    (1, 3, 1),
    (2, 5, 3);

-- Insert data into DELIVERY_INFORMATION table
INSERT INTO DELIVERY_INFORMATION (userID, province_city, delivery_address, recipient_name, email, phone_number, support_for_rush_delivery)
VALUES
    (1, 'New York', '123 Main St', 'John Doe', 'johndoe@example.com', '1234567890', 1),
    (2, 'Los Angeles', '456 Elm St', 'Jane Smith', 'janesmith@example.com', '0987654321', 0);

-- Insert data into ORDER table
INSERT INTO `ORDER` (total, total_shipping_fee, deliveryID)
VALUES
    (39.99, 5.00, 1),
    (24.99, 0.00, 2);

-- Insert data into ORDER_MEDIA table
INSERT INTO ORDER_MEDIA (orderID, mediaID, number_of_products, price)
VALUES
    (1, 1, 2, 19),
    (1, 3, 1, 14),
    (2, 5, 3, 29);

-- Insert data into RUSH_DELIVERY table
INSERT INTO RUSH_DELIVERY (rush_shipping_fee, delivery_time, delivery_instructions, orderID)
VALUES
    (10.00, '2023-01-15 10:00:00', 'Leave at front door', 1);

-- Insert data into PAYMENT_TRANSACTION table
INSERT INTO PAYMENT_TRANSACTION (transactionID, time, date, transaction_content, orderID)
VALUES
    ('TX123', '14:00:00', '2023-01-14', 'Payment for Order 1', 1),
    ('TX124', '15:00:00', '2023-01-15', 'Payment for Order 2', 2);

-- Insert data into Card table
INSERT INTO Card (cardNumber, holderName, expirationDate, securityCode, userID)
VALUES
    ('1234567812345678', 'John Doe', '2025-12-31', '123', 1),
    ('8765432187654321', 'Jane Smith', '2026-06-30', '456', 2);
