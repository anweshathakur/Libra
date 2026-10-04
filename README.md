# Libra

**Libra** is an intelligent Library Management and Personalized Book Recommendation System. It features a modern React (JavaScript + Vite) frontend, a robust Java + Spring Boot backend service, a Python FastAPI machine learning service for recommendation scoring, and a structured relational database schema.

---

## Architecture Overview

```text
       ┌────────────────────────┐
       │   React Frontend       │  (Vite + JavaScript)
       │   http://localhost:5173│
       └───────────┬────────────┘
                   │ HTTP / REST
                   ▼
       ┌────────────────────────┐
       │   Spring Boot Backend  │  (Java API Service)
       │   http://localhost:8080│
       └─────┬────────────┬─────┘
             │            │ HTTP
             │            ▼
             │   ┌────────────────────────┐
             │   │   FastAPI ML Service   │  (Python Recommendation Engine)
             │   │   http://localhost:8000│
             │   └────────────────────────┘
             ▼
       ┌────────────────────────┐
       │   Relational Database  │  (H2 / MySQL / PostgreSQL)
       │   Tables & Schemas     │
       └────────────────────────┘
```

---

## Project Structure

```text
Libra/
├── frontend/             ← React + JavaScript (Vite) client application
│   ├── src/
│   │   ├── App.jsx
│   │   ├── main.jsx
│   │   ├── App.css
│   │   └── ...
│   ├── public/
│   ├── index.html
│   ├── vite.config.js
│   └── package.json
│
├── backend/              ← Java + Spring Boot REST API
│   ├── src/
│   │   └── main/
│   │       ├── java/com/libra/backend/
│   │       └── resources/application.properties
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
│
├── ml/                   ← Python Recommendation Service
│   ├── src/
│   │   └── main.py       ← FastAPI recommendation endpoints
│   └── requirements.txt
│
├── database/             ← SQL schema, seeds & queries
│   ├── schema.sql        ← Table definitions & constraints
│   ├── seed.sql          ← Initial sample catalogue data
│   └── queries.sql       ← Analytic & operational queries
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

### 1. Frontend (React + Vite)
Built with React, JavaScript, and Vite.

* **Prerequisites**: Node.js (v18+) & npm
* **Setup**:
  ```bash
  cd frontend
  npm install
  ```
* **Run in Development**:
  ```bash
  npm run dev
  ```
* **Production Build**:
  ```bash
  npm run build
  ```

---

### 2. Backend (Java + Spring Boot)
Built with Java and Spring Boot with Spring Data JPA.

* **Prerequisites**: Java 17+ (or Java 8+ based on configured profile) & Maven (wrapper included)
* **Run (Windows)**:
  ```cmd
  cd backend
  mvnw.cmd spring-boot:run
  ```
* **Run (macOS / Linux)**:
  ```bash
  cd backend
  chmod +x mvnw
  ./mvnw spring-boot:run
  ```

---

### 3. Recommendation System (Python + FastAPI)
Delivers personalized book recommendations based on student borrowing history, search logs, ratings, and view activity.

* **Prerequisites**: Python 3.8+
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
* **Run Service**:
  ```bash
  uvicorn src.main:app --reload --port 8000
  ```
* **Interactive API Docs**: `http://localhost:8000/docs`

---

### 4. Database
The `database/` folder contains standard SQL scripts:
* `schema.sql`: DDL statements defining tables (`students`, `books`, `book_copies`, `borrow_records`, `reservations`, `ratings`, `search_history`, `book_views`, etc.).
* `seed.sql`: Sample records to populate initial testing data.
* `queries.sql`: Pre-built queries for reports, analytics, and circulation lookups.

---

## Documentation Links

For deep dives into the system architecture and domain models:
- [Entities & Relationships Design](docs/entities-relationships.md)
- [Entity Relationship (ER) Diagram](docs/er-diagram.md)
- [Object-Oriented Design](docs/oop-design.md)
