package com.libra.entity;

import java.util.HashSet;
import java.util.Set;

public class Book {
    private Long bookId;
    private String title;
    private String isbn;
    private String description;
    private String publisher;
    private Integer publishedYear;
    private Set<Author> authors = new HashSet<>();
    private Set<Category> categories = new HashSet<>();

    public Book() {
    }

    public Book(Long bookId, String title, String isbn, String description, String publisher, Integer publishedYear) {
        this.bookId = bookId;
        this.title = title;
        this.isbn = isbn;
        this.description = description;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
    }

    public Book(String title, String isbn, String description, String publisher, Integer publishedYear) {
        this.title = title;
        this.isbn = isbn;
        this.description = description;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author) {
        this.authors.add(author);
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishedYear=" + publishedYear +
                '}';
    }
}
