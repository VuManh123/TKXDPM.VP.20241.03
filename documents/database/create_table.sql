-- Schema: aims
CREATE DATABASE IF NOT EXISTS aims;
USE aims;

-- 1. Table: User
CREATE TABLE `USER` (
    `id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    `name` VARCHAR(45) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    `address` VARCHAR(45) NOT NULL,
    `phone` VARCHAR(45) NOT NULL,
    `user_type` INT NOT NULL,
    `password` VARCHAR(20) NOT NULL,
    `status` INT NOT NULL
);

-- 2. Media-related tables
CREATE TABLE `MEDIA` (
    `id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    `type` VARCHAR(45) NOT NULL,
    `category` VARCHAR(45) NOT NULL,
    `price` INT NOT NULL,
    `quantity` INT NOT NULL,
    `title` VARCHAR(45) NOT NULL,
    `value` INT NOT NULL,
    `imageUrl` VARCHAR(255) NOT NULL,
    `support_for_rush_delivery` INT NOT NULL
);

CREATE TABLE `CD` (
    `id` INT PRIMARY KEY NOT NULL,
    `artist` VARCHAR(45) NOT NULL,
    `recordLabel` VARCHAR(45) NOT NULL,
    `musicType` VARCHAR(45) NOT NULL,
    `releasedDate` DATE,
    CONSTRAINT `fk_cd_media` FOREIGN KEY (`id`) REFERENCES `MEDIA` (`id`)
);

CREATE TABLE `BOOKS` (
    `id` INT PRIMARY KEY NOT NULL,
    `author` VARCHAR(45) NOT NULL,
    `coverType` VARCHAR(45) NOT NULL,
    `publisher` VARCHAR(45) NOT NULL,
    `publishDate` DATETIME NOT NULL,
    `numOfPages` INT NOT NULL,
    `language` VARCHAR(45) NOT NULL,
    `bookCategory` VARCHAR(45) NOT NULL,
    CONSTRAINT `fk_book_media` FOREIGN KEY (`id`) REFERENCES `MEDIA` (`id`)
);

CREATE TABLE `DVD` (
    `id` INT PRIMARY KEY NOT NULL,
    `discType` VARCHAR(45) NOT NULL,
    `director` VARCHAR(45) NOT NULL,
    `runtime` INT NOT NULL,
    `studio` VARCHAR(45) NOT NULL,
    `subtitle` VARCHAR(45) NOT NULL,
    `language` VARCHAR(20),
    `releasedDate` DATETIME,
    `filmType` VARCHAR(45) NOT NULL,
    CONSTRAINT `fk_dvd_media` FOREIGN KEY (`id`) REFERENCES `MEDIA` (`id`)
);

-- Table: Cart
CREATE TABLE `CART` (
    `cartID` INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    `userID` INT NOT NULL,
    CONSTRAINT `fk_cart_user` FOREIGN KEY (`userID`) REFERENCES `USER` (`id`)
);

-- Table: Cart_Media
CREATE TABLE `CART_MEDIA` (
    `cartID` INT NOT NULL,
    `mediaID` INT NOT NULL,
    `number_of_products` INT,
    CONSTRAINT `fk_cart_cartmedia` FOREIGN KEY (`cartID`) REFERENCES `CART` (`cartID`),
    CONSTRAINT `fk_media_cartmedia` FOREIGN KEY (`mediaID`) REFERENCES `MEDIA` (`id`)
);

-- Table: Delivery_information
CREATE TABLE `DELIVERY_INFORMATION` (
    `deliveryID` INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    `userID` INT NOT NULL,
    `province_city` VARCHAR(255),
    `delivery_address` VARCHAR(255),
    `recipient_name` VARCHAR(255),
    `email` VARCHAR(255),
    `phone_number` VARCHAR(20),
    `support_for_rush_delivery` INT,
    CONSTRAINT `fk_delivery_user` FOREIGN KEY (`userID`) REFERENCES `USER` (`id`)
);

-- Table: Order
CREATE TABLE `ORDER` (
    `orderID` INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    `total` DECIMAL(10, 2),
    `total_shipping_fee` DECIMAL(10, 2),
    `deliveryID` INT NOT NULL,
    FOREIGN KEY (`deliveryID`) REFERENCES `DELIVERY_INFORMATION` (`deliveryID`)
);

-- Table: Order_Media
CREATE TABLE `ORDER_MEDIA` (
    `orderID` INT NOT NULL,
    `mediaID` INT NOT NULL,
    `number_of_products` INT,
    `price` INT,
    CONSTRAINT `fk_order_ordermedia` FOREIGN KEY (`orderID`) REFERENCES `ORDER` (`orderID`),
    CONSTRAINT `fk_media_ordermedia` FOREIGN KEY (`mediaID`) REFERENCES `MEDIA` (`id`)
);

-- Table: Rush_delivery
CREATE TABLE `RUSH_DELIVERY` (
    `rush_shipping_fee` DECIMAL(10, 2),
    `delivery_time` DATETIME,
    `delivery_instructions` VARCHAR(255),
    `orderID` INT NOT NULL,
    FOREIGN KEY (`orderID`) REFERENCES `ORDER` (`orderID`)
);

-- Table: Transaction
CREATE TABLE `PAYMENT_TRANSACTION` (
    `transactionID` VARCHAR(255) NOT NULL PRIMARY KEY,
    `time` TIME,
    `date` DATE,
    `transaction_content` TEXT,
    `orderID` INT NOT NULL,
    CONSTRAINT `fk_order_transaction` FOREIGN KEY (`orderID`) REFERENCES `ORDER` (`orderID`)
);

CREATE TABLE `Card` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `cardNumber` VARCHAR(45) NOT NULL,
  `holderName` VARCHAR(45) NOT NULL,
  `expirationDate` DATE NOT NULL,
  `securityCode` VARCHAR(45) NOT NULL,
  `userID` INT NOT NULL,
  CONSTRAINT `fk_card_user`
    FOREIGN KEY (`userID`)
    REFERENCES `User`(`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

