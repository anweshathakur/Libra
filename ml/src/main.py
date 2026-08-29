from fastapi import FastAPI
from pydantic import BaseModel
from typing import List

app = FastAPI(title="Libra Recommendation API", version="1.0")

class RecommendationRequest(BaseModel):
    user_id: int
    num_recommendations: int = 5

class BookRecommendation(BaseModel):
    book_id: str
    title: str
    author: str
    confidence: float

@app.get("/")
def read_root():
    return {"message": "Welcome to the Libra Recommendation API. Go to /docs for Swagger documentation."}

@app.post("/recommend", response_model=List[BookRecommendation])
def get_recommendations(request: RecommendationRequest):
    # Dummy mock recommendations
    mock_books = [
        {"book_id": "9780141439518", "title": "Pride and Prejudice", "author": "Jane Austen", "confidence": 0.95},
        {"book_id": "9780451524935", "title": "1984", "author": "George Orwell", "confidence": 0.89},
        {"book_id": "9780743273565", "title": "The Great Gatsby", "author": "F. Scott Fitzgerald", "confidence": 0.84},
        {"book_id": "9780345339683", "title": "The Hobbit", "author": "J.R.R. Tolkien", "confidence": 0.78},
        {"book_id": "9780061120084", "title": "To Kill a Mockingbird", "author": "Harper Lee", "confidence": 0.72}
    ]
    return mock_books[:request.num_recommendations]
