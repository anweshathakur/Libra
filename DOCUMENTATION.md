# Libra — Project & Code Documentation

## 1. Project Overview & Architecture

**Libra** is a Java Swing desktop application for managing library book records (Librarian CRUD panel) and student circulation (Borrowing, Returning, and Reserving books).

All source files belong to the `frontend` package inside [`src/frontend/`](src/frontend/) and share a single persistent text file, [`books.txt`](books.txt), in the project root.

```mermaid
flowchart LR
    subgraph Disk["Persistent Storage"]
        TXT["books.txt\n(CSV Lines)"]
    end

    subgraph Memory["In-Memory Model"]
        BOOK["Book.java\n(Object Model)"]
        LIST["ArrayList&lt;Book&gt; bookList"]
    end

    subgraph GUI["Java Swing Frontends"]
        LMS["LibraryManagementSystem.java\n(One-Panel CRUD GUI)"]
        SPA["StudentPanelApp.java\n(Two-Panel Student GUI)"]
    end

    TXT -- "loadDataFromFile()\n(Scanner)" --> LIST
    BOOK -- "Stored inside" --> LIST
    LIST -- "saveDataToFile()\n(FileWriter + PrintWriter)" --> TXT
    LIST <--> LMS
    LIST <--> SPA
```

---

## 2. How the Model Class (`Book.java`) Works & Is Used

[`Book.java`](src/frontend/Book.java) is the core domain class representing a single book record.

### 2.1 Attributes & Automatic Status Calculation
The class encapsulates **6 private fields** (`src/frontend/Book.java`, lines 4–9):
- `bookId` (`String`) — Unique identifier (e.g., `"B-1001"`)
- `title` (`String`) — Book title
- `author` (`String`) — Author name
- `category` (`String`) — Genre/category
- `copies` (`int`) — Number of available copies
- `status` (`String`) — Automatically computed field (`"Available"` or `"Out of stock"`)

When a `Book` object is constructed, the constructor takes 5 inputs (`bookId`, `title`, `author`, `category`, `copies`) and automatically computes `this.status` using `calculateStatus(int count)` (`src/frontend/Book.java`, lines 11–25):

```java
public Book(String bookId, String title, String author, String category, int copies) {
    this.bookId = bookId;
    this.title = title;
    this.author = author;
    this.category = category;
    this.copies = copies;
    this.status = calculateStatus(this.copies);
}

private String calculateStatus(int count) {
    if (count > 0) {
        return "Available";
    }
    return "Out of stock";
}
```

### 2.2 How `Book` Objects Are Used Across the Project
1. **In-Memory Storage (`ArrayList<Book> bookList`)**:
   Both [`LibraryManagementSystem.java`](src/frontend/LibraryManagementSystem.java) (line 36) and [`StudentPanelApp.java`](src/frontend/StudentPanelApp.java) (line 38) maintain `private ArrayList<Book> bookList = new ArrayList<>();`. Every row in the Swing `JTable` corresponds at the same index `i` to `bookList.get(i)`.
2. **Populating the `JTable` (`addRow`)**:
   Whenever a `Book` is created (either loaded from [`books.txt`](books.txt) or added via the form), `addRow(Book book)` appends the `Book` object to `bookList` and uses its getter methods (`getBookId()`, `getTitle()`, `getAuthor()`, `getCategory()`, `getCopies()`, `getStatus()`) to add a row to `DefaultTableModel`.
3. **Updating Copy Counts (`setCopies`)**:
   In [`StudentPanelApp.java`](src/frontend/StudentPanelApp.java), when a student borrows or returns a book, `book.setCopies(...)` (`src/frontend/Book.java`, lines 51–54) updates `copies` and automatically recalculates `status` (`"Available"` vs `"Out of stock"`).

---

## 3. How File Reading & Writing (`books.txt`) Works

All persistent data is stored in [`books.txt`](books.txt) as comma-separated lines (`Book ID,Title,Author,Category,Copies`):

```text
B-1001,The Overstory,Richard Powers,Fiction,3
B-1002,Sapiens,Yuval Noah Harari,History,4
B-1003,Braiding Sweetgrass,Robin Wall Kimmerer,Nature,0
B-1004,The Design of Everyday Things,Don Norman,Design,2
```

### 3.1 Reading from `books.txt` at Startup (`loadDataFromFile`)
Called inside the constructor of both [`LibraryManagementSystem.java`](src/frontend/LibraryManagementSystem.java) (lines 219–246) and [`StudentPanelApp.java`](src/frontend/StudentPanelApp.java) (lines 246–273):
1. Checks if `new File("books.txt")` exists.
2. Opens a `java.util.Scanner` to read the file line by line.
3. Splits each non-empty line on `","` (`String[] parts = line.split(",")`).
4. Parses the 5 fields (`id`, `title`, `author`, `category`, and `Integer.parseInt(parts[4].trim())`), instantiates a `new Book(...)`, and calls `addRow(book)` to populate both `bookList` and the GUI table.
5. Wraps file I/O in a `try-catch` block that displays `"Error reading from text file"` if the file is malformed.

```java
private void loadDataFromFile() {
    File file = new File("books.txt");
    if (!file.exists()) {
        return;
    }

    try {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.length() > 0) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String id = parts[0].trim();
                    String title = parts[1].trim();
                    String author = parts[2].trim();
                    String category = parts[3].trim();
                    int copies = Integer.parseInt(parts[4].trim());
                    Book book = new Book(id, title, author, category, copies);
                    addRow(book);
                }
            }
        }
        scanner.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error reading from text file");
    }
}
```

### 3.2 Writing Updates to `books.txt` (`saveDataToFile`)
Handled by `saveDataToFile(boolean showMessage)` in [`LibraryManagementSystem.java`](src/frontend/LibraryManagementSystem.java) (lines 249–264) and [`StudentPanelApp.java`](src/frontend/StudentPanelApp.java) (lines 276–291):

```java
private void saveDataToFile(boolean showMessage) {
    try {
        FileWriter fw = new FileWriter("books.txt");
        PrintWriter pw = new PrintWriter(fw);
        for (int i = 0; i < bookList.size(); i++) {
            Book book = bookList.get(i);
            pw.println(book.getBookId() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.getCategory() + "," + book.getCopies());
        }
        pw.close();
        if (showMessage) {
            JOptionPane.showMessageDialog(this, "Table data saved to books.txt");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error saving data to text file");
    }
}
```

- **How it works**: `FileWriter("books.txt")` wrapped in a `PrintWriter` iterates through every `Book` object currently in `bookList` and writes its fields separated by commas.
- **When it runs**:
  - **Automatically (`showMessage = false`)**: Triggered immediately after `addBook()`, `updateBook()`, `deleteBook()`, `borrowBook()`, and `returnBook()` so any change in the table is immediately persisted.
  - **Manually (`showMessage = true`)**: Triggered when the user clicks the **Save** button, showing a confirmation popup (`"Table data saved to books.txt"`).

---

## 4. Code Breakdown by Function

### 4.1 One-Panel System: [`LibraryManagementSystem.java`](src/frontend/LibraryManagementSystem.java)

| Method / Code Block | Lines | Responsibility & Validations Handled |
| :--- | :--- | :--- |
| `LibraryManagementSystem()` | `43–164` | Builds the JFrame window, `GridBagLayout` input form (`Book ID`, `Title`, `Author`, `Category`, `Copies`), action buttons (`Add`, `Search`, `Update`, `Delete`, `Save`, `Clear`), `JTable`, calls `loadDataFromFile()`, and attaches a `MouseAdapter` to copy selected row values into the text fields. |
| `addFormRow(...)` & `createButton(...)` | `167–198` | UI helper methods for placing label/field pairs in `GridBagLayout` and styling colored buttons. |
| `actionPerformed(ActionEvent e)` | `202–216` | Central `ActionListener` dispatcher routing button clicks to `addBook()`, `searchBook()`, `updateBook()`, `deleteBook()`, `saveDataToFile(true)`, or `clearFields()`. |
| `isInteger(String str)` | `280–291` | Character-by-character validation checking that a string is non-empty and contains only digits (`'0'`–`'9'`). |
| `addBook()` | `294–339` | Validates that no field is empty, `Book ID` length ≤ 10, `Book ID` does not already exist in `bookList`, and `Copies` is a valid non-negative integer ≤ 7 digits. Creates a `new Book(...)`, adds it to `bookList` and `tableModel`, and saves to `books.txt`. |
| `searchBook()` | `342–375` | Searches `bookList` by `Book ID` (or `Title` if ID is blank). Highlights and scrolls to the matching row in `bookTable` and populates the form fields. |
| `updateBook()` | `378–438` | Ensures a table row is selected, runs all field/ID/digit validations (ensuring no *other* row `i != row` uses the same `Book ID`), replaces the `Book` at `bookList.set(row, book)`, updates all 6 columns in `tableModel`, and writes to `books.txt`. |
| `deleteBook()` | `441–464` | Ensures a row is selected, prompts a `JOptionPane.YES_NO_OPTION` confirmation dialog, removes the record from both `bookList` and `tableModel`, and updates `books.txt`. |
| `clearFields()` | `467–474` | Deselects any table row and resets all 5 input text fields. |

---

### 4.2 Two-Panel System: [`StudentPanelApp.java`](src/frontend/StudentPanelApp.java)

| Method / Code Block | Lines | Responsibility & Validations Handled |
| :--- | :--- | :--- |
| `StudentPanelApp()` | `46–191` | Builds the two-panel interface: top form (`Student ID`, `Student Name`, `Book ID`, `Search Title`), action buttons (`Search`, `Borrow`, `Return`, `Reserve`, `Save`, `Clear`), and a 2-row `GridLayout` holding the **Available Books Catalogue** table on top and **My Borrowed & Reserved Books** table on the bottom. |
| `loadDataFromFile()` & `saveDataToFile(...)` | `246–291` | Loads the book catalogue from `books.txt` into `bookList` and `catalogueModel` on startup, and writes updated copy counts back to `books.txt`. |
| `borrowBook()` | `346–384` | Validates `Student ID` (non-empty, ≤ 10 chars), `Student Name`, and `Book ID`. Checks that the book exists and `book.getCopies() > 0` (otherwise prompts to use **Reserve**). Decrements copies via `book.setCopies(book.getCopies() - 1)`, updates the top catalogue table, appends a `"Borrowed"` record (`BR-1`, `BR-2`, ...) to the bottom table, and saves `books.txt`. |
| `returnBook()` | `387–414` | Verifies a row with status `"Borrowed"` is selected in the bottom table, increments the corresponding book's copies via `book.setCopies(book.getCopies() + 1)`, updates the catalogue table status to `"Available"`, marks the bottom record as `"Returned"`, and saves `books.txt`. |
| `reserveBook()` | `417–446` | Validates student and book fields, locates the `Book` in `bookList`, and adds a `"Reserved"` entry (`RES-1`, `RES-2`, ...) to the bottom table. |
