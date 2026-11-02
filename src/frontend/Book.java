package frontend;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private int copies;
    private String status;

    public Book(String bookId, String title, String author, String category, int copies) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be empty");
        }
        if (copies < 0) {
            throw new IllegalArgumentException("Copies cannot be negative");
        }
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.copies = copies;
        this.status = calculateStatus(this.copies);
    }

    private String calculateStatus(int count) {
        if (count > 0) {
            return "Available";
        }
        return "Out of stock";
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public int getCopies() {
        return copies;
    }

    public String getStatus() {
        return status;
    }

    public void setCopies(int copies) {
        if (copies < 0) {
            throw new IllegalArgumentException("Copies cannot be negative");
        }
        this.copies = copies;
        this.status = calculateStatus(this.copies);
    }
}
