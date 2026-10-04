# Libra — Entity Relationship (ER) Diagram

This document contains the visual Entity Relationship diagram and schema breakdown for the **Libra** Smart Library & Recommendation System.

---

## 1. Visual ER Diagram (Mermaid)

```mermaid
erDiagram
    STUDENT {
        int student_id PK
        string name
        string email
        string department
    }

    BOOK {
        int book_id PK
        string title
        string isbn
        string description
        string publisher
        int published_year
    }

    BOOK_COPY {
        int copy_id PK
        int book_id FK
        string status
        date acquisition_date
    }

    AUTHOR {
        int author_id PK
        string name
    }

    CATEGORY {
        int category_id PK
        string name
        string description
    }

    BORROW_RECORD {
        int borrow_id PK
        int student_id FK
        int copy_id FK
        date issue_date
        date due_date
        date return_date
        string status
    }

    BOOK_RATING {
        int rating_id PK
        int student_id FK
        int book_id FK
        int rating
        string review_text
        datetime created_at
    }

    RESERVATION {
        int reservation_id PK
        int student_id FK
        int book_id FK
        datetime reservation_date
        string status
    }

    SEARCH_HISTORY {
        int search_id PK
        int student_id FK
        string search_query
        datetime timestamp
    }

    BOOK_VIEW {
        int view_id PK
        int student_id FK
        int book_id FK
        datetime timestamp
    }

    BOOK_AUTHOR {
        int book_id PK, FK
        int author_id PK, FK
    }

    BOOK_CATEGORY {
        int book_id PK, FK
        int category_id PK, FK
    }

    %% Relationships
    BOOK ||--o{ BOOK_COPY : "has copies"
    BOOK_COPY ||--o{ BORROW_RECORD : "borrowed in"
    STUDENT ||--o{ BORROW_RECORD : "borrows"

    STUDENT ||--o{ BOOK_RATING : "rates"
    BOOK ||--o{ BOOK_RATING : "rated in"

    STUDENT ||--o{ RESERVATION : "reserves"
    BOOK ||--o{ RESERVATION : "target of"

    STUDENT ||--o{ SEARCH_HISTORY : "searches"

    STUDENT ||--o{ BOOK_VIEW : "views"
    BOOK ||--o{ BOOK_VIEW : "viewed in"

    BOOK ||--o{ BOOK_AUTHOR : "has"
    AUTHOR ||--o{ BOOK_AUTHOR : "author of"

    BOOK ||--o{ BOOK_CATEGORY : "categorized by"
    CATEGORY ||--o{ BOOK_CATEGORY : "groups"
```

---

## 2. Cardinality Summary

| Parent Entity | Relationship | Child / Related Entity | Description |
| :--- | :---: | :--- | :--- |
| **`STUDENT`** | `1 : N` | **`BORROW_RECORD`** | One student has many borrow transactions over time. |
| **`BOOK_COPY`** | `1 : N` | **`BORROW_RECORD`** | A physical copy can appear across multiple borrow histories (1 active at a time). |
| **`BOOK`** | `1 : N` | **`BOOK_COPY`** | One catalogue book title maps to multiple physical copies. |
| **`STUDENT`** | `N : M` | **`BOOK`** *(via `BOOK_RATING`)* | Students rate books; books receive ratings from many students. |
| **`STUDENT`** | `N : M` | **`BOOK`** *(via `RESERVATION`)* | Students reserve book titles; titles have multiple reservations. |
| **`STUDENT`** | `1 : N` | **`SEARCH_HISTORY`** | One student performs multiple query searches. |
| **`STUDENT`** | `N : M` | **`BOOK`** *(via `BOOK_VIEW`)* | Interaction signal recording when a student views a book. |
| **`BOOK`** | `N : M` | **`AUTHOR`** *(via `BOOK_AUTHOR`)* | Books can have multiple authors; authors can write multiple books. |
| **`BOOK`** | `N : M` | **`CATEGORY`** *(via `BOOK_CATEGORY`)* | Books can belong to multiple categories; categories group books. |

---

## 3. Key Design Notes

1. **Physical Copy vs. Conceptual Title**:
   - `BORROW_RECORD` points directly to `copy_id` (`BOOK_COPY`), reflecting physical inventory control.
   - `RESERVATION` points to `book_id` (`BOOK`), as students reserve any available copy of a title.
2. **Recommendation Signals**:
   - `SEARCH_HISTORY`, `BOOK_VIEW`, `BOOK_RATING`, `BORROW_RECORD`, and `RESERVATION` serve as interaction data points for training and evaluating collaborative/content-based filtering in the ML module.
