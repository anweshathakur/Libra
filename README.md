# Libra

**Libra** is a Library Management and Book Recommendation System coursework project. It consists of a Java Swing desktop application interface, a core Java OOP domain backend, a structured relational database schema, and documentation for machine learning recommendation extensions.

---

## Project Structure

```text
Libra/
├── swing-library-app/    ← Java Swing Desktop GUI (Graded Project)
│   ├── src/
│   │   ├── LoginFrame.java      ← Student & Librarian Login GUI (with main)
│   │   ├── BookCrudFrame.java   ← Book Inventory Table GUI (Add/Edit/Delete)
│   │   └── Book.java            ← Book UI Model
│   └── README.md
│
├── backend/              ← Core Java Backend
│   ├── src/main/java/com/libra/
│   │   ├── Main.java            ← Main Class
│   │   └── entity/              ← Plain Java OOP Entities & Enums
│   │       ├── Student.java
│   │       ├── Book.java
│   │       ├── BookCopy.java
│   │       ├── Author.java
│   │       ├── Category.java
│   │       ├── BorrowRecord.java
│   │       ├── BookRating.java
│   │       ├── Reservation.java
│   │       ├── SearchHistory.java
│   │       ├── BookView.java
│   │       ├── CopyStatus.java
│   │       ├── BorrowStatus.java
│   │       └── ReservationStatus.java
│   └── pom.xml
│
├── database/             ← SQL schema, seeds & queries
│   ├── schema.sql        ← Table definitions & constraints
│   ├── seed.sql          ← Initial sample catalogue data
│   └── queries.sql       ← Analytic & operational queries
│
├── ml/                   ← Python Recommendation Service
│   ├── src/
│   │   └── main.py       ← FastAPI recommendation endpoints
│   └── requirements.txt
│
├── docs/                 ← Architecture & Design Documentation
│   ├── entities-relationships.md  ← Entity attributes & relationship design
│   ├── er-diagram.md              ← Visual Mermaid ER diagram & cardinality
│   └── oop-design.md              ← Object-oriented domain design
│
├── README.md             ← Project overview & run instructions
└── .gitignore
```

---

## Component Setup & Running Instructions

### 1. Swing Library App (Desktop GUI)
Built with pure Java Swing (no frameworks needed).

* **Run**:
  ```bash
  cd swing-library-app/src
  javac *.java
  java LoginFrame
  ```
* **Test Accounts**:
  * **Student**: `STU-2291` *(Amara Chen)*, `STU-1187`, `STU-3305`, `STU-0942`
  * **Librarian**: `LIB-001` *(Maya Osei)*

---

### 2. Core Java Backend
Pure Java OOP domain entities and main entry point.

* **Location**: `backend/src/main/java/com/libra/`
* **Run**:
  ```bash
  cd backend/src/main/java
  javac com/libra/Main.java com/libra/entity/*.java
  java com.libra.Main
  ```

---

### 3. Database
The `database/` folder contains standard SQL scripts:
* `schema.sql`: DDL statements defining tables (`students`, `books`, `book_copies`, `borrow_records`, `reservations`, `ratings`, `search_history`, `book_views`).
* `seed.sql`: Sample records to populate initial testing data.
* `queries.sql`: Pre-built queries for reports, analytics, and circulation lookups.

---

### 4. Recommendation System (Python + FastAPI)
Delivers personalized book recommendations.

* **Setup**:
  ```bash
  cd ml
  python -m venv venv
  # Windows:
  .\venv\Scripts\activate
  # macOS/Linux:
  source venv/bin/activate

  pip install -r requirements.txt
  ```
* **Run**:
  ```bash
  uvicorn src.main:app --reload --port 8000
  ```

---

## Documentation Links

- [Entities & Relationships Design](docs/entities-relationships.md)
- [Entity Relationship (ER) Diagram](docs/er-diagram.md)
- [Object-Oriented Design](docs/oop-design.md)
