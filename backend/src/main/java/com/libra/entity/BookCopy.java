package com.libra.entity;

import java.time.LocalDate;

public class BookCopy {
    private Long copyId;
    private Book book;
    private CopyStatus status;
    private LocalDate acquisitionDate;

    public BookCopy() {
    }

    public BookCopy(Long copyId, Book book, CopyStatus status, LocalDate acquisitionDate) {
        this.copyId = copyId;
        this.book = book;
        this.status = status;
        this.acquisitionDate = acquisitionDate;
    }

    public BookCopy(Book book, CopyStatus status, LocalDate acquisitionDate) {
        this.book = book;
        this.status = status;
        this.acquisitionDate = acquisitionDate;
    }

    public Long getCopyId() {
        return copyId;
    }

    public void setCopyId(Long copyId) {
        this.copyId = copyId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public CopyStatus getStatus() {
        return status;
    }

    public void setStatus(CopyStatus status) {
        this.status = status;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    @Override
    public String toString() {
        return "BookCopy{" +
                "copyId=" + copyId +
                ", book=" + (book != null ? book.getTitle() : "null") +
                ", status=" + status +
                ", acquisitionDate=" + acquisitionDate +
                '}';
    }
}
