package com.libra.entity;

import java.time.LocalDateTime;

public class SearchHistory {
    private Long searchId;
    private Student student;
    private String searchQuery;
    private LocalDateTime timestamp;

    public SearchHistory() {
        this.timestamp = LocalDateTime.now();
    }

    public SearchHistory(Long searchId, Student student, String searchQuery, LocalDateTime timestamp) {
        this.searchId = searchId;
        this.student = student;
        this.searchQuery = searchQuery;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    public SearchHistory(Student student, String searchQuery) {
        this.student = student;
        this.searchQuery = searchQuery;
        this.timestamp = LocalDateTime.now();
    }

    public Long getSearchId() {
        return searchId;
    }

    public void setSearchId(Long searchId) {
        this.searchId = searchId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SearchHistory{" +
                "searchId=" + searchId +
                ", student=" + (student != null ? student.getName() : "null") +
                ", searchQuery='" + searchQuery + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
