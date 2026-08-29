-- Libra Useful Queries

-- 1. View all books currently on a user's shelf with status
SELECT u.username, b.title, b.author, s.status, s.added_at
FROM shelves s
JOIN users u ON s.user_id = u.id
JOIN books b ON s.book_id = b.id
WHERE u.username = 'alice';

-- 2. Fetch recommendations generated for a user with confidence scores
SELECT u.username, b.title, b.author, r.confidence, r.generated_at
FROM recommendations r
JOIN users u ON r.user_id = u.id
JOIN books b ON r.book_id = b.id
ORDER BY r.confidence DESC;

-- 3. Calculate library statistics (total books, unique titles, quantity)
SELECT 
    COUNT(*) AS unique_titles,
    SUM(quantity) AS total_physical_books,
    AVG(published_year) AS average_publication_year
FROM books;

-- 4. Find most popular books (books added to shelves most frequently)
SELECT b.title, b.author, COUNT(s.id) as shelf_count
FROM shelves s
JOIN books b ON s.book_id = b.id
GROUP BY b.id
ORDER BY shelf_count DESC;

-- 5. List users who have reading lists but no generated recommendations yet
SELECT u.id, u.username, u.email
FROM users u
LEFT JOIN recommendations r ON u.id = r.user_id
WHERE r.id IS NULL;
