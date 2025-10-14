# Shrink_IT

A **Spring Boot** web application that converts long URLs into short, shareable links and tracks usage analytics. This project provides a REST API for URL shortening, redirection and analytics, along with a user-friendly **Thymeleaf** frontend.

---

## 🚀 Features

- Shorten long URLs into unique short codes using **Base62 encoding**
- Redirect short URLs to original long URLs
- Track number of clicks for each shortened URL
- Support for optional **custom aliases**
- Centralized **error handling** with meaningful HTTP status codes
- Basic web interface using **Thymeleaf** for URL submission and analytics
- Prepared for **Docker-based deployment**

---

## 🛠️ Tech Stack

- **Backend:** Java, Spring Boot, Spring Data JPA
- **Frontend:** Thymeleaf, HTML, CSS
- **Database:** PostgreSQL
- **Containerization:** Docker

---


## Project Structure

```
url-shortener/
├── src/
│   ├── main/
│   │   ├── java/com/example/url_shortener/
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── service/         # Business logic
│   │   │   ├── model/           # Entity classes
│   │   │   └── repository/      # Data access layer
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    # Test cases
├── pom.xml
└── README.md

```
## ⚙️ Running Locally

1. Clone the repository:

```bash
git clone https://github.com/VivekT77/Url_Shortener.git
cd Url_Shortener
