package com.libra.entity;

import java.time.LocalDate;

public class BorrowRecord {
    private Long borrowId;
    private Student student;
    private BookCopy bookCopy;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BorrowStatus status;

    public BorrowRecord() {
    }

    public BorrowRecord(Long borrowId, Student student, BookCopy bookCopy, LocalDate issueDate, LocalDate dueDate, LocalDate returnDate, BorrowStatus status) {
        this.borrowId = borrowId;
        this.student = student;
        this.bookCopy = bookCopy;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public BorrowRecord(Student student, BookCopy bookCopy, LocalDate issueDate, LocalDate dueDate, BorrowStatus status) {
        this.student = student;
        this.bookCopy = bookCopy;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BorrowStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BorrowRecord{" +
                "borrowId=" + borrowId +
                ", student=" + (student != null ? student.getName() : "null") +
                ", bookCopyId=" + (bookCopy != null ? bookCopy.getCopyId() : "null") +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status=" + status +
                '}';
    }
}
