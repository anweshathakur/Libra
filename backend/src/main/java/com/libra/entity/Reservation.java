package com.libra.entity;

import java.time.LocalDateTime;

public class Reservation {
    private Long reservationId;
    private Student student;
    private Book book;
    private LocalDateTime reservationDate;
    private ReservationStatus status;

    public Reservation() {
        this.reservationDate = LocalDateTime.now();
    }

    public Reservation(Long reservationId, Student student, Book book, LocalDateTime reservationDate, ReservationStatus status) {
        this.reservationId = reservationId;
        this.student = student;
        this.book = book;
        this.reservationDate = reservationDate != null ? reservationDate : LocalDateTime.now();
        this.status = status;
    }

    public Reservation(Student student, Book book, ReservationStatus status) {
        this.student = student;
        this.book = book;
        this.reservationDate = LocalDateTime.now();
        this.status = status;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
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

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", student=" + (student != null ? student.getName() : "null") +
                ", book=" + (book != null ? book.getTitle() : "null") +
                ", reservationDate=" + reservationDate +
                ", status=" + status +
                '}';
    }
}
