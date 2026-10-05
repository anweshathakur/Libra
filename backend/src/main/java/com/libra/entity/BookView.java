package com.libra.entity;

import java.time.LocalDateTime;

public class BookView {
    private Long viewId;
    private Student student;
    private Book book;
    private LocalDateTime timestamp;

    public BookView() {
        this.timestamp = LocalDateTime.now();
    }

    public BookView(Long viewId, Student student, Book book, LocalDateTime timestamp) {
        this.viewId = viewId;
        this.student = student;
        this.book = book;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    public BookView(Student student, Book book) {
        this.student = student;
        this.book = book;
        this.timestamp = LocalDateTime.now();
    }

    public Long getViewId() {
        return viewId;
    }

    public void setViewId(Long viewId) {
        this.viewId = viewId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BookView{" +
                "viewId=" + viewId +
                ", student=" + (student != null ? student.getName() : "null") +
                ", book=" + (book != null ? book.getTitle() : "null") +
                ", timestamp=" + timestamp +
                '}';
    }
}
