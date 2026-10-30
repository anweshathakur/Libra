# Library Management System (CRUD Java Swing Application)

A Java Swing desktop application for managing library book records and student borrowing/reservations, storing `Book` objects in an `ArrayList` in memory and persisting data to `books.txt`.

## Project Structure

```text
Libra/
├── src/frontend/
│   ├── Book.java                      ← Book model class
│   ├── LibraryManagementSystem.java   ← One-panel Book CRUD GUI (Add, Search, Update, Delete, Save)
│   └── StudentPanelApp.java           ← Two-panel Student Catalogue & Borrow/Reserve GUI
├── books.txt                          ← Persistent text file storage for book records
├── .classpath
├── .project
├── .gitignore
└── README.md
```

## How to Run

From the project root directory (`Libra/`):

```bash
javac src/frontend/*.java
```

### Run One-Panel System (Librarian Book CRUD)
```bash
java -cp src frontend.LibraryManagementSystem
```

### Run Two-Panel System (Student Panel)
```bash
java -cp src frontend.StudentPanelApp
```
