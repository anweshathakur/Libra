# Libra — Object-Oriented Design (OOD)

Libra is a smart library management and personalized book recommendation system. The backend will use Java/Spring Boot with a relational database, while the recommendation system can be extended with ML later.

## 1. Core Domain Classes

### `User`

Represents a system user.

**Attributes:** `id`, `name`, `email`, `passwordHash`, `role`, `createdAt`

Roles:

* Student
* Librarian

### `Book`

Represents a book in the library catalogue.

**Attributes:** `id`, `isbn`, `title`, `description`, `publisher`, `publishedYear`

### `BookCopy`

Represents an individual physical copy of a book.

**Attributes:** `id`, `book`, `status`, `acquisitionDate`

Status: `AVAILABLE`, `ISSUED`, `RESERVED`, `MAINTENANCE`, `LOST`

### `Author`

Represents a book author.

**Attributes:** `id`, `name`

### `Category`

Represents a book subject/genre.

**Attributes:** `id`, `name`, `description`

### `BorrowRecord`

Tracks the issue and return of a book copy.

**Attributes:** `id`, `student`, `bookCopy`, `issueDate`, `dueDate`, `returnDate`, `status`

### `Reservation`

Tracks a student's request for an unavailable book.

**Attributes:** `id`, `student`, `book`, `reservationDate`, `status`

### `Fine`

Tracks fines generated from overdue borrowing.

**Attributes:** `id`, `borrowRecord`, `amount`, `reason`, `status`

### `Review`

Stores a student's rating/review of a book.

**Attributes:** `id`, `student`, `book`, `rating`, `reviewText`, `createdAt`

### `StudentInterest`

Stores subjects/categories selected by a student.

**Attributes:** `student`, `category`

### `Notification`

Stores system notifications such as due-date and reservation reminders.

**Attributes:** `id`, `student`, `type`, `message`, `createdAt`, `readStatus`

### `Recommendation`

Represents a personalized recommendation generated for a student.

**Attributes:** `id`, `student`, `book`, `score`, `generatedAt`

---

## 2. Major Relationships

```text
User ──→ Student / Librarian

Book 1 ──→ N BookCopy
Book N ──→ N Author
Book N ──→ N Category

Student 1 ──→ N BorrowRecord
BookCopy 1 ──→ N BorrowRecord

Student 1 ──→ N Reservation
Book 1 ──→ N Reservation

BorrowRecord 1 ──→ 0..1 Fine

Student 1 ──→ N Review
Book 1 ──→ N Review

Student N ──→ N Category
       (StudentInterest)

Student 1 ──→ N Notification
Student 1 ──→ N Recommendation
Book 1 ──→ N Recommendation
```

---

## 3. Main Service Classes

Business logic will be separated from the entities through service classes:

* `BookService`
* `CirculationService`
* `ReservationService`
* `FineService`
* `RecommendationService`
* `NotificationService`

Repositories will handle database persistence through Spring Data JPA.

---

## 4. OOP Concepts

The project will demonstrate:

* **Encapsulation** — controlled access to object state
* **Abstraction** — interfaces for replaceable components such as recommendation strategies
* **Inheritance** — where appropriate for `User`, `Student`, and `Librarian`
* **Polymorphism** — multiple implementations of common interfaces
* **Composition** — relationships between domain objects

---

## 5. Architecture

```text
React Frontend
      ↓
Spring Boot Controllers
      ↓
Service Layer
      ↓
JPA Repositories
      ↓
SQL Database
      ↓
Recommendation Component
```

The recommendation component will initially be simple and can later be replaced or extended with a Python/ML-based system.
