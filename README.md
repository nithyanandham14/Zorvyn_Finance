📊 Finance Backend System

A role-based backend system built using Spring Boot for managing financial records, user access, and dashboard analytics. The application provides secure authentication using JWT and enforces access control based on user roles.

🚀 Features
🔐 JWT-based Authentication & Authorization
👥 Role-Based Access Control (Admin, Analyst, Viewer)
💰 Financial Record Management (CRUD operations)
📊 Dashboard Summary APIs (totals, insights)
🔎 Filtering and data retrieval support
⚙️ Clean layered architecture (Controller, Service, Repository)
📡 External API integration (Metal rates)
🔔 Scheduler & notification support (Twilio integration)

🏗️ Tech Stack
Java 17+
Spring Boot
Spring Security
JWT (JSON Web Token)
Hibernate / JPA
MySQL / H2/ postgres (configurable)
Maven

📁 Project Structure
src/
 ├── controller
 ├── service
 ├── repo
 ├── module (entity)
 ├── config (security, jwt)
 └── scheduler
 
⚙️ Setup Instructions
1. Clone the Repository
git clone https://github.com/your-username/Finance.git
cd Finance
2. Configure Application Properties

Go to:

src/main/resources/application.properties
🔧 Database Configuration (example)
spring.datasource.url=jdbc:mysql://localhost:3306/finance_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
🔐 JWT Configuration
jwt.secret=your_secret_key

3. Install Dependencies
mvn clean install
4. Run the Application
mvn spring-boot:run

OR from IDE:
👉 Run FinanceApplication.java

5. Application URL
http://localhost:8080

🔐 Authentication Flow
1. Register User
POST /register
{
  "username": "admin",
  "password": "123",
  "role": "ADMIN"
}
2. Login
POST /login

👉 Returns JWT token

3. Use Token

Add header:

Authorization: Bearer <your_token>
👥 Roles & Permissions
Role	Permissions
ADMIN	Full access (create, update, delete, manage users)
ANALYST	View records and access insights
VIEWER	Read-only access
📊 API Overview
Financial Records
POST /items → Create record (ADMIN)
GET /items → View records (ADMIN, ANALYST)
DELETE /items/{id} → Delete record (ADMIN)
Summary APIs
GET /summary → Dashboard insights (All roles)
External APIs
GET /api/metal-rates → Fetch gold/silver rates
⚠️ Important Notes
target/ folder is excluded using .gitignore
Sensitive credentials are NOT stored in the repository
Role-based access is enforced using @PreAuthorize
JWT authentication is stateless
🧪 Testing

Use:

Postman

Steps:
Register user
Login → get token
Use token in headers
Test APIs based on role

🧠 Design Approach

The system is designed with a focus on:

Clean architecture
Separation of concerns
Secure authentication and authorization
Maintainable and scalable backend structure

👨‍💻 Author
Nithyanandham J

Contact_Details -> nithyanandham1452006@gmail.com
