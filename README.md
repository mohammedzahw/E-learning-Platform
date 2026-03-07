# E-Learning Platform

A comprehensive, backend-driven e-learning platform built with Spring Boot. This application provides robust features for course management, user authentication (including OAuth2), secure payments via PayPal, and email notifications.

## 🚀 Features

- **User Authentication & Authorization:** Secure login and registration using JWT (JSON Web Tokens) and Spring Security.
- **OAuth2 Integration:** Supports social login via Google and GitHub.
- **Course Management:** Creating, updating, and managing courses.
- **Payment Processing:** Integrated with PayPal SDK for handling course purchases and payouts.
- **Media Hosting:** Integration with Cloudinary for handling image uploads.
- **Email Notifications:** SMTP integration for sending emails (e.g., for password recovery).
- **API Documentation:** Auto-generated interactive API documentation using Springdoc OpenAPI (Swagger).

## 🛠️ Technologies Used

- **Java 21**
- **Spring Boot 3.2.5** (Web, Data JPA, Security, Mail, Validation, Actuator)
- **Database:** PostgreSQL
- **Authentication:** JWT (io.jsonwebtoken) & OAuth2 Client
- **Payment Gateway:** PayPal SDK (Core, REST API, Payouts)
- **Media Storage:** Cloudinary
- **Documentation:** Springdoc OpenAPI (Swagger UI)
- **Tooling:** Lombok, Maven

## ⚙️ Getting Started

### Prerequisites

- Java Development Kit (JDK) 21
- Maven
- PostgreSQL database

### Configuration

Before running the application, you need to configure your environment variables and API keys. Open `src/main/resources/application.properties` and update the following properties to match your local setup:

1.  **Database Configuration:**

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/e-learning-platform
    spring.datasource.username=your_postgres_username
    spring.datasource.password=your_postgres_password
    ```

2.  **JWT Secret:**

    ```properties
    auth.secret=your_strong_jwt_secret_key
    ```

3.  **Mail Server (SMTP):**

    ```properties
    spring.mail.username=your_email@gmail.com
    spring.mail.password=your_app_password
    ```

4.  **OAuth2 (Optional):**

    ```properties
    spring.security.oauth2.client.registration.github.client-id=your_github_client_id
    spring.security.oauth2.client.registration.github.client-secret=your_github_client_secret
    spring.security.oauth2.client.registration.google.client-id=your_google_client_id
    spring.security.oauth2.client.registration.google.client-secret=your_google_client_secret
    ```

5.  **PayPal Configuration (Sandbox):**
    ```properties
    paypal.client-id=your_paypal_client_id
    paypal.client-secret=your_paypal_client_secret
    paypal.mode=sandbox
    ```

### Running the Application

1.  Clone the repository:

    ```bash
    git clone <repository-url>
    cd E-learning-Platform
    ```

2.  Build the project and install dependencies:

    ```bash
    mvn clean install
    ```

3.  Run the application:
    ```bash
    mvn spring-boot:run
    ```

The application will start on port `8080`.

## 📚 API Documentation

Once the application is running, you can access the interactive API documentation and test the endpoints using Swagger UI at:

`http://localhost:8080/swagger-ui.html`
