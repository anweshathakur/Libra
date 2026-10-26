# Libra — Entities & Relationships Design (Two-Panel System)

## 1. Project Overview

**Libra** is a 2-panel library management system that manages book cataloguing, physical copy inventory, and circulation between **Students (User Panel)** and **Librarians (Admin Panel)**.

The system tracks:

* **Students** (User Panel) & **Librarians** (Admin Panel)
* **Books** (Catalogue titles) and **Physical Book Copies**
* **Borrowing and Returning** (`BorrowRecord`)
* **Book Reservations** (`Reservation`)
* **Book Ratings & Reviews** (`BookRating`)
* **Authors and Categories**

---

# 2. Entities & Panel Ownership

## 2.1 Student *(Common — User Profile & Librarian Lookup)*

Represents a student using the library system.

| Attribute    | Panel Ownership | Description                   |
| ------------ | --------------- | ----------------------------- |
| `student_id` | **Common**      | Primary key (e.g., `STU-2291`)|
| `name`       | **User Side**   | Student's full name           |
| `email`      | **User Side**   | Student email                 |
| `department` | **User Side**   | Student's academic department |

---

## 2.2 Librarian *(Librarian Panel)*

Represents a library staff member managing the system.

| Attribute      | Panel Ownership    | Description                     |
| -------------- | ------------------ | ------------------------------- |
| `librarian_id` | **Librarian Side** | Primary key (e.g., `LIB-001`)   |
| `name`         | **Librarian Side** | Librarian's name                |
| `email`        | **Librarian Side** | Staff email                     |

---

## 2.3 Book *(Common — Librarian Writes, User Reads)*

Represents a book in the library catalogue.

| Attribute        | Panel Ownership                  | Description         |
| ---------------- | -------------------------------- | ------------------- |
| `book_id`        | **Common**                       | Primary key         |
| `title`          | **Librarian Write / User Read**  | Book title          |
| `isbn`           | **Librarian Write / User Read**  | ISBN of the book    |
| `description`    | **Librarian Write / User Read**  | Short description   |
| `publisher`      | **Librarian Write / User Read**  | Publisher           |
| `published_year` | **Librarian Write / User Read**  | Year of publication |

A `Book` represents the **catalogue title itself**, not a particular physical copy.

---

## 2.4 BookCopy *(Librarian Panel — Derived Availability for User)*

Represents an individual physical copy of a book.

| Attribute          | Panel Ownership    | Description                        |
| ------------------ | ------------------ | ---------------------------------- |
| `copy_id`          | **Librarian Side** | Primary key                        |
| `book_id`          | **Common**         | Foreign key → `Book`               |
| `status`           | **Librarian Side** | `AVAILABLE`, `ISSUED`, `MAINTENANCE`, `LOST` |
| `acquisition_date` | **Librarian Side** | Date the library acquired the copy |

Separating `Book` and `BookCopy` allows the Librarian to track individual copies while the User Panel simply displays how many copies are currently `AVAILABLE`.

---

## 2.5 Author *(Librarian Panel)*

Represents an author of a book.

| Attribute   | Panel Ownership    | Description   |
| ----------- | ------------------ | ------------- |
| `author_id` | **Librarian Side** | Primary key   |
| `name`      | **Librarian Side** | Author's name |

---

## 2.6 Category *(Librarian Panel)*

Represents a subject/category associated with a book.

| Attribute     | Panel Ownership    | Description                 |
| ------------- | ------------------ | --------------------------- |
| `category_id` | **Librarian Side** | Primary key                 |
| `name`        | **Librarian Side** | Category name               |
| `description` | **Librarian Side** | Description of the category |

---

## 2.7 BorrowRecord *(Common — Librarian Issues/Returns, User Views)*

Represents one borrowing transaction.

| Attribute     | Panel Ownership                 | Description                          |
| ------------- | ------------------------------- | ------------------------------------ |
| `borrow_id`   | **Common**                      | Primary key                          |
| `student_id`  | **Common**                      | Foreign key → `Student`              |
| `copy_id`     | **Librarian Side**              | Foreign key → `BookCopy`             |
| `issue_date`  | **Librarian Write / User Read** | Date the copy was issued             |
| `due_date`    | **Librarian Write / User Read** | Expected return date                 |
| `return_date` | **Librarian Write / User Read** | Actual return date                   |
| `status`      | **Librarian Write / User Read** | `ACTIVE`, `RETURNED`, `OVERDUE`      |

---

## 2.8 Reservation *(Common — User Requests/Cancels, Librarian Fulfills)*

Represents a student's request to reserve a book when no copies are currently available.

| Attribute          | Panel Ownership                               | Description              |
| ------------------ | --------------------------------------------- | ------------------------ |
| `reservation_id`   | **Common**                                    | Primary key              |
| `student_id`       | **User Side**                                 | Foreign key → `Student`  |
| `book_id`          | **Common**                                    | Foreign key → `Book`     |
| `reservation_date` | **User Side**                                 | Date/time of reservation |
| `status`           | **Shared** (`ACTIVE`, `FULFILLED`, `CANCELLED`, `EXPIRED`) | User can cancel; Librarian fulfills |

---

## 2.9 BookRating *(User Panel)*

Stores a student's rating and review of a book.

| Attribute     | Panel Ownership | Description                   |
| ------------- | --------------- | ----------------------------- |
| `rating_id`   | **User Side**   | Primary key                   |
| `student_id`  | **User Side**   | Foreign key → `Student`       |
| `book_id`     | **Common**      | Foreign key → `Book`          |
| `rating`      | **User Side**   | Rating (1–5) given by student |
| `review_text` | **User Side**   | Optional written review       |
| `created_at`  | **User Side**   | Date/time of rating           |

Constraint: `UNIQUE(student_id, book_id)`

---

# 3. Junction Tables *(Librarian Panel)*

## 3.1 BookAuthor
Connects `Book` and `Author` (`BOOK N ─── N AUTHOR`).
* Primary Key: `(book_id, author_id)`

## 3.2 BookCategory
Connects `Book` and `Category` (`BOOK N ─── N CATEGORY`).
* Primary Key: `(book_id, category_id)`

---

# 4. Final Entity List

### Core Entities
1. `Student` *(Common)*
2. `Librarian` *(Librarian Panel)*
3. `Book` *(Common)*
4. `BookCopy` *(Librarian Panel)*
5. `Author` *(Librarian Panel)*
6. `Category` *(Librarian Panel)*
7. `BorrowRecord` *(Common)*
8. `Reservation` *(Common)*
9. `BookRating` *(User Panel)*

### Junction Tables
10. `BookAuthor` *(Librarian Panel)*
11. `BookCategory` *(Librarian Panel)*
