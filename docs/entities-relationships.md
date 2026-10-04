# Libra — Entities & Relationships Design

## 1. Project Overview

**Libra** is a smart library management system that manages book circulation while collecting student activity data for personalized book recommendations.

The system tracks:

* Students
* Books and physical book copies
* Borrowing and returning
* Ratings
* Reservations
* Search activity
* Book views
* Authors and categories

The collected activity data can later be used by the recommendation engine to suggest relevant books to students.

---

# 2. Entities

## 2.1 Student

Represents a student using the library system.

| Attribute    | Description                   |
| ------------ | ----------------------------- |
| `student_id` | Primary key                   |
| `name`       | Student's name                |
| `email`      | Student email                 |
| `department` | Student's academic department |

A student can search for books, view books, borrow books, reserve books, and rate books.

---

## 2.2 Book

Represents a book in the library catalogue.

| Attribute        | Description         |
| ---------------- | ------------------- |
| `book_id`        | Primary key         |
| `title`          | Book title          |
| `isbn`           | ISBN of the book    |
| `description`    | Short description   |
| `publisher`      | Publisher           |
| `published_year` | Year of publication |

A `Book` represents the **book itself**, not a particular physical copy.

For example:

```text
Book: Clean Code

Copies:
C001
C002
C003
```

---

## 2.3 BookCopy

Represents an individual physical copy of a book.

| Attribute          | Description                        |
| ------------------ | ---------------------------------- |
| `copy_id`          | Primary key                        |
| `book_id`          | Foreign key → Book                 |
| `status`           | Current physical status            |
| `acquisition_date` | Date the library acquired the copy |

Possible statuses:

```text
AVAILABLE
ISSUED
MAINTENANCE
LOST
```

Separating `Book` and `BookCopy` allows the system to correctly determine availability.

---

## 2.4 Author

Represents an author of a book.

| Attribute   | Description   |
| ----------- | ------------- |
| `author_id` | Primary key   |
| `name`      | Author's name |

An author can write multiple books, and a book can have multiple authors.

---

## 2.5 Category

Represents a subject/category associated with a book.

| Attribute     | Description                 |
| ------------- | --------------------------- |
| `category_id` | Primary key                 |
| `name`        | Category name               |
| `description` | Description of the category |

Examples:

```text
Artificial Intelligence
Database Systems
Web Development
Fiction
Computer Networks
```

A book can belong to multiple categories.

---

## 2.6 BorrowRecord

Represents one borrowing transaction.

| Attribute     | Description              |
| ---------------- | ------------------------ |
| `borrow_id`   | Primary key              |
| `student_id`  | Foreign key → Student    |
| `copy_id`     | Foreign key → BookCopy   |
| `issue_date`  | Date the copy was issued |
| `due_date`    | Expected return date     |
| `return_date` | Actual return date       |
| `status`      | Current borrowing status |

Possible statuses:

```text
ACTIVE
RETURNED
OVERDUE
```

A borrowing record refers to a **BookCopy**, not directly to a Book.

This allows the system to know exactly which physical copy was issued.

---

## 2.7 BookRating

Stores a student's rating of a book.

| Attribute     | Description             |
| ------------- | ----------------------- |
| `rating_id`   | Primary key             |
| `student_id`  | Foreign key → Student   |
| `book_id`     | Foreign key → Book      |
| `rating`      | Rating given by student |
| `review_text` | Optional written review |
| `created_at`  | Date/time of rating     |

A student should normally have only one active rating/review for a particular book.

A suitable constraint is:

```text
UNIQUE(student_id, book_id)
```

---

## 2.8 Reservation

Represents a student's request to reserve a book.

| Attribute          | Description              |
| ------------------ | ------------------------ |
| `reservation_id`   | Primary key              |
| `student_id`       | Foreign key → Student    |
| `book_id`          | Foreign key → Book       |
| `reservation_date` | Date/time of reservation |
| `status`           | Reservation status       |

Possible statuses:

```text
ACTIVE
FULFILLED
CANCELLED
EXPIRED
```

Reservations are made for a **Book**, not a specific `BookCopy`.

The student is interested in obtaining any available copy of that book.

---

## 2.9 SearchHistory

Stores searches performed by a student.

| Attribute      | Description           |
| -------------- | --------------------- |
| `search_id`    | Primary key           |
| `student_id`   | Foreign key → Student |
| `search_query` | Search text           |
| `timestamp`    | Time of search        |

Example:

```text
Student searches: "machine learning"
```

This can later be used as a signal for the recommendation engine.

---

## 2.10 BookView

Records when a student views a book.

| Attribute    | Description           |
| ------------ | --------------------- |
| `view_id`    | Primary key           |
| `student_id` | Foreign key → Student |
| `book_id`    | Foreign key → Book    |
| `timestamp`  | Time of view          |

Book views provide another signal of student interest.

---

# 3. Junction Tables

Some relationships are many-to-many. These require junction tables in the relational database.

## 3.1 BookAuthor

Connects books and authors.

| Attribute   | Description          |
| ----------- | -------------------- |
| `book_id`   | Foreign key → Book   |
| `author_id` | Foreign key → Author |

Primary key:

```text
(book_id, author_id)
```

Relationship:

```text
BOOK N ─────── N AUTHOR
        via
    BOOK_AUTHOR
```

---

## 3.2 BookCategory

Connects books and categories.

| Attribute     | Description            |
| ------------- | ---------------------- |
| `book_id`     | Foreign key → Book     |
| `category_id` | Foreign key → Category |

Primary key:

```text
(book_id, category_id)
```

Relationship:

```text
BOOK N ─────── N CATEGORY
        via
    BOOK_CATEGORY
```

---

# 4. Relationships and Cardinality

## 4.1 Student → BorrowRecord

```text
STUDENT 1 ───────── N BORROW_RECORD
```

One student can have many borrowing records.

Each borrowing record belongs to exactly one student.

---

## 4.2 BookCopy → BorrowRecord

```text
BOOK_COPY 1 ───────── N BORROW_RECORD
```

A physical copy can appear in multiple borrowing records over its lifetime.

However, it can have only **one active borrowing record at a time**.

Example:

```text
Copy C001

Borrow #1 → returned
Borrow #2 → returned
Borrow #3 → currently active
```

---

## 4.3 Book → BookCopy

```text
BOOK 1 ───────── N BOOK_COPY
```

One book can have multiple physical copies.

Example:

```text
Clean Code
   ├── C001
   ├── C002
   └── C003
```

---

## 4.4 Student → BookRating

```text
STUDENT 1 ───────── N BOOK_RATING
```

A student can rate multiple books.

Each rating belongs to one student.

---

## 4.5 Book → BookRating

```text
BOOK 1 ───────── N BOOK_RATING
```

A book can receive ratings from many students.

Together:

```text
STUDENT N ───────── N BOOK
             via
         BOOK_RATING
```

---

## 4.6 Student → Reservation

```text
STUDENT 1 ───────── N RESERVATION
```

A student can make multiple reservations over time.

---

## 4.7 Book → Reservation

```text
BOOK 1 ───────── N RESERVATION
```

A book can have reservations from multiple students.

Together:

```text
STUDENT N ───────── N BOOK
             via
         RESERVATION
```

---

## 4.8 Student → SearchHistory

```text
STUDENT 1 ───────── N SEARCH_HISTORY
```

A student can perform many searches.

Each search record belongs to one student.

---

## 4.9 Student → BookView

```text
STUDENT 1 ───────── N BOOK_VIEW
```

A student can view many books.

---

## 4.10 Book → BookView

```text
BOOK 1 ───────── N BOOK_VIEW
```

A book can be viewed by many students.

Together:

```text
STUDENT N ───────── N BOOK
             via
          BOOK_VIEW
```

---

## 4.11 Book ↔ Author

```text
BOOK N ───────── N AUTHOR
          via
      BOOK_AUTHOR
```

A book can have multiple authors.

An author can write multiple books.

---

## 4.12 Book ↔ Category

```text
BOOK N ───────── N CATEGORY
          via
      BOOK_CATEGORY
```

A book can belong to multiple categories.

A category can contain multiple books.

---

# 5. Complete Relationship Map

```text
                         AUTHOR
                           ▲
                           │
                           N
                           │
                     BOOK_AUTHOR
                           │
                           N
                           │
                         BOOK
                      /    │    \
                     /     │     \
                    /      │      \
                   ▼       ▼       ▼
             BOOK_COPY  BOOK_VIEW  RESERVATION
                 │          ▲          ▲
                 │          │          │
                 │          │          │
                 ▼          │          │
           BORROW_RECORD    │          │
                 ▲           │          │
                 │           │          │
                 │           │          │
              STUDENT ──────┘──────────┘
                 │
          ┌──────┼──────────┐
          │      │          │
          ▼      ▼          ▼
      SEARCH   RATING   BORROW_RECORD
      HISTORY
```

And separately:

```text
BOOK N ─────── N CATEGORY
          │
          │
     BOOK_CATEGORY
```

---

# 6. Recommendation Engine Data Flow

The recommendation system does not need to treat all activities equally.

It can receive signals such as:

```text
Student
   │
   ├── Search History
   ├── Book Views
   ├── Borrow History
   ├── Ratings
   └── Reservations
             │
             ▼
      Recommendation Engine
             │
             ▼
      Recommended Books
```

For example, if a student:

```text
Searches → "Machine Learning"
Views → ML books
Borrows → ML book
Rates → ML book highly
```

the recommendation engine can infer stronger interest in that subject.

The exact recommendation algorithm can be designed later.

---

# 7. Final Entity List

### Main entities

1. `Student`
2. `Book`
3. `BookCopy`
4. `Author`
5. `Category`
6. `BorrowRecord`
7. `BookRating`
8. `Reservation`
9. `SearchHistory`
10. `BookView`

### Junction tables

11. `BookAuthor`
12. `BookCategory`

---

# 8. Important Design Decisions

### Book ≠ BookCopy

`Book` represents the catalogue entry.

`BookCopy` represents the physical copy.

This allows Libra to track the actual availability of every copy.

### BorrowRecord references BookCopy

We don't store both `book_id` and `copy_id` in `BorrowRecord`.

The book can be obtained through:

```text
BorrowRecord → BookCopy → Book
```

This avoids unnecessary duplication.

### Reservations reference Book

A student reserves a title, not a particular physical copy.

### Activity is stored separately

Searches and views are kept as separate records because they can be useful recommendation signals.

### Availability is derived

We don't need:

```text
Book.available_quantity
```

Instead, availability comes from the statuses of its `BookCopy` records.

---

# 9. Future Entities

These can be added after the core system works:

```text
Fine
Notification
Recommendation
Librarian
```

They are intentionally not part of the first database version so that the core library workflow remains manageable.

The recommendation table is also optional because recommendations can either be generated dynamically or stored for recommendation history/analytics.
