-- Insert data into MEDIA table
INSERT INTO MEDIA (type, category, price, quantity, title, value, imageUrl, support_for_rush_delivery)
VALUES
    ('Book', 'Fiction', 19000, 10, 'The Great Gatsby', 100, 'images/book/book1.jpg', 1),
    ('Book', 'Non-Fiction', 24000, 15, 'Sapiens', 200, 'images/book/book2.jpg', 0),
    ('DVD', 'Movie', 14000, 20, 'Inception', 150, 'images/dvd/dvd1.jpg', 1),
    ('CD', 'Music', 9000, 25, 'Thriller', 120, 'images/cd/cd1.jpg', 0),
    ('CD', 'Vinyl', 29000, 5, 'Abbey Road', 250, 'images/cd/cd2.jpg', 1),
    ('Book', 'Fiction', 18000, 8, '1984', 180, 'images/book/book3.jpg', 0),
    ('DVD', 'Series', 39000, 12, 'Game of Thrones Season 1', 400, 'images/dvd/dvd2.jpg', 1),
    ('CD', 'Music', 11000, 18, 'Back in Black', 130, 'images/cd/cd3.jpg', 0),
    ('Book', 'Vinyl', 25000, 7, 'The Wall', 300, 'images/book/book4.jpg', 1),
    ('Book', 'Non-Fiction', 22000, 10, 'Educated', 220, 'images/book/book5.jpg', 0);

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

