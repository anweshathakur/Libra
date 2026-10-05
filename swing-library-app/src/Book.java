public class Book {
    private String id;
    private String isbn;
    private String title;
    private String author;
    private String category;
    private int copies;

    public Book(String id, String isbn, String title, String author, String category, int copies) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.copies = copies;
    }

    public String getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getCopies() { return copies; }

    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setCopies(int copies) { this.copies = copies; }
}
