# 🎯 AI-Based Recommendation System

This project is built as part of the **CodTech Internship (Task-4)**.  
It is a **Java + Spring Boot** application that implements an **AI-based Recommendation Engine** using **Apache Mahout** for collaborative filtering, with a pure Java fallback recommender to ensure reliability.

---

## 📘 Project Overview

The system recommends products or content based on user preferences.  
It uses a **user-based collaborative filtering algorithm** powered by **Apache Mahout**, and automatically falls back to a cosine similarity–based recommender if Mahout is unavailable or the dataset is small.

Users can query the recommender through a REST API endpoint to get personalized suggestions.

---

## ⚙️ Tech Stack

| Component | Technology |
|------------|-------------|
| Language | Java 17+ |
| Framework | Spring Boot |
| Library | Apache Mahout (v0.9) |
| Build Tool | Maven |
| IDE | Spring Tool Suite (STS) |
| Data Format | CSV (userID,itemID,rating) |

---

## 🧩 Features

✅ User-based collaborative filtering (Mahout)  
✅ Fallback recommender using cosine similarity (pure Java)  
✅ REST API endpoint for recommendations  
✅ Modular, extensible design with Spring dependency injection  
✅ Configurable dataset path through `application.properties`  

---

## 📁 Project Structure

AI-Recommendation-System/
├── src/
│ ├── main/java/com/codtech/recommend/
│ │ ├── Application.java
│ │ ├── config/RecommenderConfig.java
│ │ ├── controller/RecommendationController.java
│ │ └── recommender/
│ │ ├── RecommenderService.java
│ │ ├── MahoutRecommenderService.java
│ │ └── FallbackRecommender.java
│ └── resources/
│ ├── application.properties
│ └── data/ratings.csv
├── pom.xml
└── README.md

markdown
Copy code

---

## 🧠 How It Works

1. **Mahout Engine (Primary):**
   - Loads `ratings.csv` into a `DataModel`
   - Computes similarity between users using **Pearson correlation**
   - Finds the **nearest neighbors**
   - Recommends items the user hasn’t rated yet

2. **Fallback Engine (Backup):**
   - Loads the same CSV into a `Map`
   - Calculates **cosine similarity** between users
   - Produces weighted item recommendations

3. **REST Controller:**
   - Exposes endpoint  
     ```
     GET /recommend/{userId}?topK=5
     ```
   - Returns recommendations as JSON.

---

## ⚡ Sample Data (`ratings.csv`)

Example:
1,101,5.0
1,102,3.5
2,101,4.0
2,103,4.5
3,104,5.0
3,101,4.0
4,105,4.5

yaml
Copy code

---

## 🚀 Running the Application

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/<your-username>/AI-Recommendation-System.git
cd AI-Recommendation-System
2️⃣ Import Project into STS
Open Spring Tool Suite

File → Import → Existing Maven Projects

Select the project folder

3️⃣ Run the Application
Right-click Application.java → Run As → Spring Boot App

4️⃣ Test the API
Open a browser or Postman:

bash
Copy code
GET http://localhost:8080/recommend/1?topK=3
Sample response:

json
Copy code
{
  "103": 4.6,
  "104": 4.2,
  "105": 4.0
}
🔧 Configuration
You can set the dataset path in application.properties:

ini
Copy code
recommender.data.path=./src/main/resources/data/ratings.csv
server.port=8080
🧩 Dependencies (pom.xml)
Key dependencies:

xml
Copy code
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
  <groupId>org.apache.mahout</groupId>
  <artifactId>mahout-core</artifactId>
  <version>0.9</version>
</dependency>
🧪 Example Output
Console log:

pgsql
Copy code
Mahout recommender initialized with data: /path/to/ratings.csv
Recommending top 5 items for user: 1
Recommendations: {103=4.6, 104=4.2, 105=4.0}
🧑‍💻 Author
Name: [Your Full Name]
Internship: CodTech Internship – Task-4
Project: AI-Based Recommendation System using Apache Mahout
IDE: Spring Tool Suite (STS)

🏁 Summary
This project demonstrates:

The use of AI techniques in Java for personalized recommendations

Integration of Apache Mahout for scalable collaborative filtering

Real-world software design with Spring Boot REST APIs

📌 A production-ready, error-free, and modular recommendation engine suitable for academic or internship submission.
