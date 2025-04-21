# Openverse Media Search

A full-stack web application for searching Creative Commons media via the Openverse API. Built with modern web technologies including React, Spring Boot, PostgreSQL, and Docker, with secure JWT and Google OAuth2 authentication.

---

Live Preview (Local Docker)

Frontend: [http://localhost:3000](http://localhost:3000)

Backend: [http://localhost:8080](http://localhost:8080)  

---

## Key Features


- **Media Search** – Query images and audio using the Openverse API
  
-  **User Auth** – Secure login/register with JWT + session cookie
  
-  **Google OAuth2 Login** – Login via your Google account
  
-  **Search History** – View and delete your past queries
  
-  **Fully Dockerized** – Backend, frontend, and PostgreSQL orchestrated via Docker Compose
  
-  **JUnit Testing** – Comprehensive backend tests using Spring Boot & JUnit 5
  
-  **CI/CD Ready** – GitHub workflows configured for building & deploying
  
-  **Modular Codebase** – Clean separation of concerns with Java packages and React components

---

## Tech Stack

| Layer        | Technology                         |

|-------------|-------------------------------------|

| Frontend     | React, Tailwind CSS, Axios          |

| Backend      | Java 21, Spring Boot 3.4.3          |

| Database     | PostgreSQL                          |

| Auth         | JWT + Google OAuth2 via Spring Security |

| Testing      | JUnit 5, Spring Boot Test           |

| DevOps       | Docker, Docker Compose, GitHub Actions |

---

## Prerequisites

- Docker & Docker Compose installed
  
- (Optional) Java 21 & Node.js if running locally without Docker

---

## Getting Started

### Run with Docker (Recommended)


```bash

# Clone the repository

git clone https://github.com/raifaisalhussain/openverse-media-search.git

cd openverse-media-search


**# Run all services**

1. Full Clean Build

Make sure you're in your root project folder

docker compose down -v --remove-orphans

# Clean any previous builds

./mvnw clean
________________________________________

2. Build React Frontend

Ensure you’re in the frontend folder:

# Install dependencies (if not done)

npm install

# Build the frontend app

npm run build

# Go back to project root

cd ..
________________________________________

3. Build Backend JAR
Make sure your Spring Boot JAR (mediaapp-0.0.1-SNAPSHOT.jar) is built and copied properly to the Docker build context.

From root folder:

./mvnw clean package -DskipTests

COPY target/mediaapp-0.0.1-SNAPSHOT.jar app.jar

________________________________________

4. Build Docker Images

From project root:

docker compose build
________________________________________

5. Run the Entire Stack

From the root:

docker compose up -d
________________________________________

6. View Logs

You can follow logs using:

docker compose logs -f

Or just the backend:

docker logs -f media-backend

And frontend:

docker logs -f openverse-frontend

To stop service:

docker-compose down
---

### Run Manually

Backend:

cd backend

./mvnw clean install

./mvnw spring-boot:run

Frontend:

cd frontend

npm install

npm start

Set the API base URL in .env:

REACT_APP_API_BASE_URL=http://localhost:8080
---

**Environment Variables**

backend/src/main/resources/application.properties

spring.security.oauth2.client.registration.google.client-id=your-client-id
spring.security.oauth2.client.registration.google.client-secret=your-client-secret

**Running Backend Tests**

All test classes can be found on: 

openverse-media-search\backend\src\test\java\com\mediaapp

From the root:

mvn -Dtest=TestClassName test

Includes:

Service layer tests

Controller tests

Integration tests with mock environment

**Project Structure**

Backend

└── com.mediaapp
    ├── config
    ├── controller
    ├── model
    ├── repository
    ├── security
    ├── service
    ├── util
    └── exception

Frontend

└── src
    ├── components
    ├── contexts
    ├── pages
    ├── utils

**Docker Services**

Service | Description | Port
backend | Spring Boot API | 8080
frontend | React App + Nginx | 3000
db | PostgreSQL | 5432

**Project Highlights**

Uses Spring Security with custom JWT filter and Google success handler

CORS and CSRF configured for frontend/backend coordination

Health checks via /actuator/health

Environment-based configuration fallback support

All pages are mobile responsive using Tailwind

**Future Enhancements**

Advanced filters (e.g. date)

Pagination caching

Rate limiting & brute-force protection

i18n (multi-language support)


**Acknowledgements**

Openverse API

Spring Boot

Tailwind CSS

Create React App


---
Faisal Hussain
