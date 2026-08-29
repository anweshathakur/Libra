-- Libra Seed Data

-- Insert sample users
INSERT INTO users (username, email, password_hash, role) VALUES 
('alice', 'alice@libra.com', '$2a$10$abcdef1234567890abcdefu', 'USER'),
('bob', 'bob@libra.com', '$2a$10$abcdef1234567890abcdefv', 'USER'),
('admin_user', 'admin@libra.com', '$2a$10$abcdef1234567890abcdefw', 'ADMIN');

-- Insert sample books
INSERT INTO books (isbn, title, author, genre, published_year, quantity) VALUES
('9780141439518', 'Pride and Prejudice', 'Jane Austen', 'Classic Literature', 1813, 5),
('9780451524935', '1984', 'George Orwell', 'Dystopian Fiction', 1949, 3),
('9780743273565', 'The Great Gatsby', 'F. Scott Fitzgerald', 'Classic Literature', 1925, 4),
('9780345339683', 'The Hobbit', 'J.R.R. Tolkien', 'Fantasy', 1937, 6),
('9780061120084', 'To Kill a Mockingbird', 'Harper Lee', 'Classic Literature', 1960, 4),
('9780140283334', 'The Odyssey', 'Homer', 'Epic Poetry', -800, 2),
('9780307474278', 'The Da Vinci Code', 'Dan Brown', 'Mystery Thriller', 2003, 7);

-- Insert sample shelf configurations
-- Alice: Pride and Prejudice (COMPLETED), 1984 (READING), The Hobbit (WANT_TO_READ)
INSERT INTO shelves (user_id, book_id, status) VALUES
(1, 1, 'COMPLETED'),
(1, 2, 'READING'),
(1, 4, 'WANT_TO_READ');

-- Bob: The Great Gatsby (COMPLETED), The Hobbit (READING)
INSERT INTO shelves (user_id, book_id, status) VALUES
(2, 3, 'COMPLETED'),
(2, 4, 'READING');

-- Insert sample recommendations
INSERT INTO recommendations (user_id, book_id, confidence) VALUES
(1, 3, 0.9500), -- Recommend Gatsby to Alice
(1, 5, 0.8800), -- Recommend Mockingbird to Alice
(2, 1, 0.9200), -- Recommend Pride & Prejudice to Bob
(2, 5, 0.8500); -- Recommend Mockingbird to Bob
