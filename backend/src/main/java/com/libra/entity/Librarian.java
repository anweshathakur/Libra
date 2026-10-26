package com.libra.entity;

public class Librarian {
    private Long librarianId;
    private String name;
    private String email;

    public Librarian() {
    }

    public Librarian(Long librarianId, String name, String email) {
        this.librarianId = librarianId;
        this.name = name;
        this.email = email;
    }

    public Librarian(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Long getLibrarianId() {
        return librarianId;
    }

    public void setLibrarianId(Long librarianId) {
        this.librarianId = librarianId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Librarian{" +
                "librarianId=" + librarianId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
