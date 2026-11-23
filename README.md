# Spring Boot Session-Based Authentication

A backend authentication system built using **Java, Spring Boot, Spring Security, and MySQL**. It provides user registration, login, logout, and protected API endpoints using server-side HTTP sessions.

## 🚀 Features

- User registration with input validation
- Secure password hashing using BCrypt
- User login and authentication using Spring Security
- Session-based authentication using HTTP sessions
- CSRF protection
- Protected user profile endpoint
- User logout and session invalidation
- Global exception handling
- MySQL database integration

## 🛠️ Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot
- **Security:** Spring Security
- **Database:** MySQL
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Maven
- **Validation:** Jakarta Bean Validation
- **API Testing:** Postman

## 📁 Project Structure

```text
src/main/java/com/authentication/demo/
│
├── config/
│   └── SecurityConfig.java
│
├── controller/
│   ├── AuthController.java
│   └── GlobalExceptionHandler.java
│
├── dto/
│   ├── SignupRequest.java
│   ├── LoginRequest.java
│   └── AuthResponse.java
│
├── entity/
│   └── User.java
│
├── repository/
│   └── UserRepository.java
│
├── service/
│   ├── AuthService.java
│   └── CustomUserDetailsService.java
│
└── AuthenticationSystemApplication.java
```

## 🔐 Authentication Flow

```text
             Client
               |
               | Login Request
               v
         AuthController
               |
               v
       AuthenticationManager
               |
               v
       DaoAuthenticationProvider
               |
               v
    CustomUserDetailsService
               |
               v
          UserRepository
               |
               v
           MySQL DB
               |
               v
       Password Verification
               |
               v
       SecurityContext Created
               |
               v
        HTTP Session Created
               |
               v
        JSESSIONID Cookie
               |
               v
       Authenticated User
```

The server stores the authenticated user's security context in the HTTP session. The client uses the `JSESSIONID` cookie to identify its session on subsequent requests.

## 🔗 API Endpoints

Base URL: `http://localhost:8080`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/auth/csrf` | Get CSRF token |
| POST | `/api/auth/signup` | Register a new user |
| POST | `/api/auth/login` | Login user |
| GET | `/api/auth/me` | Get authenticated user |
| POST | `/api/auth/logout` | Logout user |

### Signup Request

```json
{
  "name": "Rishikesh",
  "email": "rishikesh@gmail.com",
  "password": "YourPassword@123"
}
```

### Login Request

```json
{
  "email": "rishikesh@gmail.com",
  "password": "YourPassword@123"
}
```

### Successful Login Response

```json
{
  "message": "Login successful",
  "name": "Rishikesh",
  "email": "rishikesh@gmail.com"
}
```

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/rishikeshsingh906/spring-boot-session-authentication.git
```

### 2. Open the Project

Open the cloned project in IntelliJ IDEA.

### 3. Create MySQL Database

```sql
CREATE DATABASE authentication_db;
```

### 4. Configure Database Credentials

Set the `DB_PASSWORD` environment variable to your MySQL password.

Configure `application.properties`:

```properties
spring.application.name=authentication-system
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/authentication_db
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 5. Run the Application

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The application will start at:

`http://localhost:8080`

## 🧪 API Testing

You can test the authentication APIs using Postman.

Recommended testing sequence:

1. Get the CSRF token.
2. Register a new user.
3. Get a fresh CSRF token if required.
4. Login with registered credentials.
5. Access the protected `/api/auth/me` endpoint.
6. Logout and verify that the protected endpoint rejects unauthenticated requests.

**Important:** Keep cookies enabled in Postman so that the session cookie is sent with subsequent requests.

## 🔒 Security Concepts Implemented

- `UserDetails` – Represents user security information.
- `UserDetailsService` – Loads user details from the database.
- `AuthenticationManager` – Coordinates authentication.
- `DaoAuthenticationProvider` – Verifies user credentials.
- `PasswordEncoder` – Securely hashes and verifies passwords.
- `SecurityContext` – Stores the current authentication information.
- `HttpSession` – Maintains the user's authenticated session.
- `CSRF Protection` – Protects against cross-site request forgery.

## 👨‍💻 Author

**Rishikesh Kumar Singh**

- GitHub: [rishikeshsingh906](https://github.com/rishikeshsingh906)

---

⭐ If you find this project useful, consider giving it a star!
