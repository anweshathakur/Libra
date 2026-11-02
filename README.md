# Library Management System (CRUD Java Swing Application)

A Java Swing desktop application for managing library book records (Librarian CRUD panel) and student borrowing/reservations (Student Two-Panel system), storing `Book` objects in an `ArrayList` in memory, handling errors with structured Java exception handling (`try-catch-finally`), and persisting data to `books.txt`.

---

## 1. Project Structure

```text
Libra/
├── src/frontend/
│   ├── Book.java                      ← Book model class (fields, validation, status calculation, getters/setters)
│   ├── LibraryManagementSystem.java   ← One-panel Book CRUD GUI (Add, Search, Update, Delete, Save, Clear)
│   └── StudentPanelApp.java           ← Two-panel Student Catalogue & Borrow/Reserve GUI
├── books.txt                          ← Persistent comma-separated text file for book records
├── DOCUMENTATION.md                   ← Detailed architecture, code & exception handling documentation
├── .classpath
├── .project
├── .gitignore
└── README.md
```

---

## 2. How the Model Class (`Book.java`) Is Used

`Book.java` (`package frontend;`) represents a single book entity in the system:
- **Fields**: `bookId` (`String`), `title` (`String`), `author` (`String`), `category` (`String`), `copies` (`int`), and `status` (`String`).
- **Domain Validation & Automatic Status Calculation**: When a `Book` object is created (`new Book(id, title, author, category, copies)`), its constructor validates that `bookId` is non-empty and `copies >= 0` (throwing `IllegalArgumentException` otherwise) and automatically computes `status` using `calculateStatus(int count)` — returning `"Available"` if `copies > 0` or `"Out of stock"` if `copies == 0`.
- **In-Memory Storage (`ArrayList<Book> bookList`)**: Both `LibraryManagementSystem` and `StudentPanelApp` maintain an `ArrayList<Book> bookList` in memory. Each row in the Swing `JTable` corresponds to the `Book` object at the same index in `bookList`.
- **Dynamic Updates (`setCopies`)**: When a student borrows or returns a book in `StudentPanelApp`, `book.setCopies(...)` validates `copies >= 0`, updates the copy count, and automatically recalculates the book's `status`.

---

## 3. How File Reading & Writing (`books.txt`) Works

Book records are persisted in `books.txt` in CSV format (`Book ID,Title,Author,Category,Copies`):

```text
B-1001,The Overstory,Richard Powers,Fiction,3
B-1002,Sapiens,Yuval Noah Harari,History,4
B-1003,Braiding Sweetgrass,Robin Wall Kimmerer,Nature,0
B-1004,The Design of Everyday Things,Don Norman,Design,2
```

1. **Loading at Startup (`loadDataFromFile()`)**:
   - Uses `java.io.File` and `java.util.Scanner` inside a `try-catch-finally` block to read `books.txt` line by line when the window opens.
   - Splits each line by `,`, parses the integer copy count inside an inner `try-catch (NumberFormatException | IllegalArgumentException ex)` block, creates a `new Book(...)` instance, and calls `addRow(book)` to populate both `bookList` and the `JTable`.
   - Catches `FileNotFoundException` and closes the `Scanner` in the `finally` block.
2. **Saving Changes (`saveDataToFile(boolean showMessage)`)**:
   - Uses `java.io.FileWriter` and `java.io.PrintWriter` inside a `try-catch-finally` block (catching `IOException`) to iterate over `bookList` and write every `Book` back to `books.txt`.
   - Called **automatically** after every Add, Update, Delete, Borrow, or Return operation so `books.txt` always stays in sync with the table, and **manually** when clicking the **Save** button (which displays `"Table data saved to books.txt"`).

---

## 4. Exception Handling

- **`IllegalArgumentException`**: Thrown and caught across `Book`, `LibraryManagementSystem`, and `StudentPanelApp` when required fields are blank, `Book ID` or `Student ID` exceeds 10 characters, a duplicate `Book ID` is entered, `Copies` is negative, no table row is selected prior to Update/Delete/Return, or a book is out of stock when borrowing.
- **`NumberFormatException`**: Thrown by `Integer.parseInt(...)` and caught in `addBook()`, `updateBook()`, and `loadDataFromFile()` when non-integer text is entered in the `Copies` field.
- **`FileNotFoundException` & `IOException`**: Caught in `loadDataFromFile()` and `saveDataToFile(...)` during file I/O operations, with `finally` blocks ensuring `Scanner` and `PrintWriter` resources are always closed.

---

## 5. How to Run

```bash
javac -d bin src/frontend/*.java
```

### Run One-Panel System (Librarian Book CRUD)
```bash
java -cp bin frontend.LibraryManagementSystem
```

### Run Two-Panel System (Student Panel)
```bash
java -cp bin frontend.StudentPanelApp
```
