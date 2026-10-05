package com.libra.entity;

import java.time.LocalDateTime;

public class BookRating {
    private Long ratingId;
    private Student student;
    private Book book;
    private Integer rating;
    private String reviewText;
    private LocalDateTime createdAt;

    public BookRating() {
        this.createdAt = LocalDateTime.now();
    }

    public BookRating(Long ratingId, Student student, Book book, Integer rating, String reviewText, LocalDateTime createdAt) {
        this.ratingId = ratingId;
        this.student = student;
        this.book = book;
        this.rating = rating;
        this.reviewText = reviewText;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public BookRating(Student student, Book book, Integer rating, String reviewText) {
        this.student = student;
        this.book = book;
        this.rating = rating;
        this.reviewText = reviewText;
        this.createdAt = LocalDateTime.now();
    }

    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "BookRating{" +
                "ratingId=" + ratingId +
                ", student=" + (student != null ? student.getName() : "null") +
                ", book=" + (book != null ? book.getTitle() : "null") +
                ", rating=" + rating +
                ", reviewText='" + reviewText + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
