# Libra

Libra is an intelligent Library Management and Book Recommendation System. It features a React-based frontend, a Java + Spring Boot backend service, and a Python-based machine learning model to generate personalized book recommendations.

## Project Structure

```text
Libra/
│
├── frontend/          ← React + TypeScript (Vite) project
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── ...
│
├── backend/            ← Java + Spring Boot (Maven) project
│   ├── src/
│   ├── pom.xml
│   └── ...
│
├── ml/                 ← Recommendation system (Python)
│   ├── src/
│   ├── models/
│   ├── data/
│   └── requirements.txt
│
├── database/           ← SQL scripts/schema
│   ├── schema.sql
│   ├── seed.sql
│   └── queries.sql
│
├── docs/               ← Project documentation
│   ├── requirements.md
│   ├── er-diagram.png
│   ├── architecture.png
│   └── oop-design.md
│
├── README.md
└── .gitignore
```

---

## Component Setup & Running Instructions

### 1. Frontend (React + Vite)
The user interface is built using React, TypeScript, and Vite.

* **Setup**:
  ```bash
  cd frontend
  npm install
  ```
* **Run**:
  ```bash
  npm run dev
  ```
* **Build**:
  ```bash
  npm run build
  ```

### 2. Backend (Java + Spring Boot)
The API service is built with Java and Spring Boot. It uses a Maven wrapper to ensure consistent builds.

* **Prerequisites**: Java 8 (or higher)
* **Setup & Run**:
  On Windows:
  ```cmd
  cd backend
  mvnw.cmd spring-boot:run
  ```
  On macOS/Linux:
  ```bash
  cd backend
  chmod +x mvnw
  ./mvnw spring-boot:run
  ```

### 3. Machine Learning (Python Recommendation System)
The ML system serves personalized book recommendations using collaborative and content-based filtering.

* **Prerequisites**: Python 3.8+ (tested on Python 3.14)
* **Setup**:
  ```bash
  cd ml
  py -m venv venv
  # Activate venv on Windows:
  .\venv\Scripts\activate
  # Install dependencies:
  pip install -r requirements.txt
  ```
* **Run (FastAPI dev server)**:
  ```bash
  uvicorn src.main:app --reload --port 8000
  ```

### 4. Database Setup
Database DDL and initial seeds are located in the `database/` directory.
- `schema.sql`: Contains schemas for tables like `books`, `users`, `shelves`, and `recommendations`.
- `seed.sql`: Contains initial data to populate the library database.
- `queries.sql`: Handy queries for retrieving reports and join details.

---

## Documentation
Refer to the `docs/` folder for detailed specifications:
- `docs/requirements.md`: System requirements.
- `docs/oop-design.md`: Object-Oriented design and design patterns.
