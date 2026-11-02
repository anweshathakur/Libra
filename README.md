# Library Management System (CRUD Java Swing Application)

A Java Swing desktop application for managing library book records (Librarian CRUD panel) and student borrowing/reservations (Student Two-Panel system), storing `Book` objects in an `ArrayList` in memory and persisting data to `books.txt`.

---

## 1. Project Structure

```text
Libra/
├── src/frontend/
│   ├── Book.java                      ← Book model class (fields, status calculation, getters/setters)
│   ├── LibraryManagementSystem.java   ← One-panel Book CRUD GUI (Add, Search, Update, Delete, Save, Clear)
│   └── StudentPanelApp.java           ← Two-panel Student Catalogue & Borrow/Reserve GUI
├── books.txt                          ← Persistent comma-separated text file for book records
├── .classpath
├── .project
├── .gitignore
└── README.md
```

---

## 2. How the Model Class (`Book.java`) Is Used

`Book.java` (`package frontend;`) represents a single book entity in the system:
- **Fields**: `bookId` (`String`), `title` (`String`), `author` (`String`), `category` (`String`), `copies` (`int`), and `status` (`String`).
- **Automatic Status Calculation**: When a `Book` object is created (`new Book(id, title, author, category, copies)`), its constructor automatically computes `status` using `calculateStatus(int count)` — returning `"Available"` if `copies > 0` or `"Out of stock"` if `copies == 0`.
- **In-Memory Storage (`ArrayList<Book> bookList`)**: Both `LibraryManagementSystem` and `StudentPanelApp` maintain an `ArrayList<Book> bookList` in memory. Each row in the Swing `JTable` corresponds to the `Book` object at the same index in `bookList`.
- **Dynamic Updates (`setCopies`)**: When a student borrows or returns a book in `StudentPanelApp`, `book.setCopies(...)` updates the copy count and automatically recalculates the book's `status`.

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
   - Uses `java.io.File` and `java.util.Scanner` to read `books.txt` line by line when the window opens.
   - Splits each line by `,`, parses the integer copy count, creates a `new Book(...)` instance, and calls `addRow(book)` to populate both `bookList` and the `JTable`.
2. **Saving Changes (`saveDataToFile(boolean showMessage)`)**:
   - Uses `java.io.FileWriter` and `java.io.PrintWriter` to iterate over `bookList` and write every `Book` back to `books.txt`.
   - Called **automatically** after every Add, Update, Delete, Borrow, or Return operation so `books.txt` always stays in sync with the table, and **manually** when clicking the **Save** button (which displays `"Table data saved to books.txt"`).

---

## 4. Functions & Code Breakdown

### `LibraryManagementSystem.java` (One-Panel Librarian CRUD System)
- **`LibraryManagementSystem()` (Constructor)**: Sets up the form inputs (`Book ID`, `Title`, `Author`, `Category`, `Copies`), buttons (`Add`, `Search`, `Update`, `Delete`, `Save`, `Clear`), `JTable`, loads initial data via `loadDataFromFile()`, and registers a `MouseAdapter` so clicking a table row fills the form fields.
- **`actionPerformed(ActionEvent e)`**: Routes button clicks to the corresponding handler method.
- **`isInteger(String str)`**: Validates that the `Copies` input contains only numeric digits (`'0'`–`'9'`).
- **`addBook()`**: Checks for empty fields, ensures `Book ID` ≤ 10 characters and is unique across `bookList`, verifies `Copies` is a valid non-negative integer ≤ 7 digits, adds the new `Book` to `bookList` and `tableModel`, and updates `books.txt`.
- **`searchBook()`**: Searches `bookList` by `Book ID` or `Title`, selects and scrolls to the matching row, and fills the form fields.
- **`updateBook()`**: Validates the edited fields for the currently selected table row, replaces the `Book` object at `bookList.set(row, book)`, updates the table cells, and saves to `books.txt`.
- **`deleteBook()`**: Prompts for confirmation (`JOptionPane.YES_NO_OPTION`), removes the selected book from `bookList` and `tableModel`, and updates `books.txt`.
- **`clearFields()`**: Clears the form inputs and table selection.

### `StudentPanelApp.java` (Two-Panel Student System)
- **`StudentPanelApp()` (Constructor)**: Builds the top student/book input form, action buttons (`Search`, `Borrow`, `Return`, `Reserve`, `Save`, `Clear`), the top **Available Books Catalogue** table, and the bottom **My Borrowed & Reserved Books** table.
- **`borrowBook()`**: Validates student and book fields, verifies `book.getCopies() > 0`, decrements the book's copies (`book.setCopies(book.getCopies() - 1)`), updates the top catalogue table, adds a `"Borrowed"` record to the bottom table, and saves the updated copy count to `books.txt`.
- **`returnBook()`**: Checks that a `"Borrowed"` row is selected in the bottom table, increments the book's copies in `bookList` and the top table, updates the bottom row status to `"Returned"`, and saves `books.txt`.
- **`reserveBook()`**: Validates student and book inputs and appends a `"Reserved"` record to the bottom table.

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
