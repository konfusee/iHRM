# iHRM - Human Resource Management System

**Group 2 - Class: SE1960-NJ**

## Overview
iHRM is a comprehensive Human Resource Management System built with Jakarta EE 10, designed to streamline HR operations and employee management processes. The system features secure authentication, employee management, attendance tracking, task management, and payroll processing.

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0 database

### Setup
1. **Clone the repository**
```bash
git clone https://github.com/konfusee/iHRM.git
cd iHRM
```

2. **Configure database connection**
```bash
# Copy the example environment file
cp .env.example .env

# Edit .env with your database credentials
# DB_HOST=your_database_host
# DB_PORT=3306
# DB_NAME=your_database_name
# DB_USERNAME=your_username
# DB_PASSWORD=your_password
```

3. **Run the application**
```bash
# Run with embedded Jetty server (no installation needed!)
./mvnw jetty:run

# Access the application
open http://localhost:8080/ihrm
```

### Default Login Credentials
Create test users by running:
```bash
# Compile first, then run
./mvnw clean compile exec:java -Dexec.mainClass=com.ihrm.web.util.TestDataInitializer
```

This creates:
- **Admin**: `admin@ihrm.com` / `admin123` (SystemRole: ADMIN)
- **Employee**: `john.doe@ihrm.com` / `password123` (SystemRole: EMPLOYEE)

## Key Features

### User Roles (SystemRole)
- **ADMIN**: Full system administration and user management
- **HR_MANAGER**: HR team supervision with full HR capabilities
- **EMPLOYEE**: Personal information access and requests
- **MANAGER**: Department management and task assignment

### Core Modules
- ✅ **Authentication & Security**: SHA-256 password hashing with salt, session-based auth
- ✅ **Employee Management**: Personal info, department/role assignments
- ✅ **Attendance Tracking**: Work schedules, daily check-in/out
- ✅ **Task Management**: Multi-employee task assignments with responses
- ✅ **Request Management**: Employee requests with approval workflow
- ✅ **Payroll & Compensation**: Contracts, base salary, allowances, receipts
- ✅ **Recruitment**: Job listings and application management
- ✅ **System Configuration**: Configurable settings via key-value store

## Technology Stack
- **Language**: Java 17
- **Framework**: Jakarta EE 10 (Servlet-based)
- **Database**: MySQL 8.0 with HikariCP connection pooling
- **Pattern**: MVC with DAO pattern (no ORM, pure JDBC)
- **Security**: SHA-256 password hashing with salt
- **UI**: JSP with JSTL 3.0, responsive CSS
- **Build Tool**: Maven 3
- **Dev Server**: Jetty 11 (embedded)
- **Production**: Tomcat 10.1+
- **Configuration**: Environment variables via dotenv-java

## 📁 Project Structure

```
iHRM/
├── src/main/java/me/konfuse/ihrm/
│   ├── dao/          # Data Access Objects
│   │   ├── DatabaseConnection.java
│   │   └── EmployeeDAO.java
│   ├── filter/       # Servlet filters
│   │   └── AuthenticationFilter.java
│   ├── model/        # Entity models (18 tables)
│   │   ├── Employee.java
│   │   ├── SystemRole.java
│   │   ├── Department.java
│   │   └── ... (15 more models)
│   ├── servlet/      # HTTP servlets
│   │   ├── LoginServlet.java
│   │   └── LogoutServlet.java
│   └── util/         # Utility classes
│       └── PasswordUtil.java
├── src/main/webapp/
│   ├── login.jsp     # Login page
│   ├── dashboard.jsp # Main dashboard
│   ├── index.jsp     # Redirects to login
│   └── WEB-INF/
│       └── web.xml   # Web app configuration
└── pom.xml          # Maven configuration
```

## 🔧 Development

### Build Commands
```bash
# Compile the project
./mvnw clean compile

# Run tests
./mvnw test

# Package as WAR
./mvnw clean package

# Run with auto-reload (changes deploy in 10 seconds)
./mvnw jetty:run
```

### Database Connection
```bash
# Using usql (universal database CLI) with environment variables
usql mysql://$DB_USERNAME:$DB_PASSWORD@$DB_HOST:$DB_PORT/$DB_NAME
```

## 📦 Deployment

### Development
```bash
# Uses embedded Jetty - no installation required
./mvnw jetty:run
# Access at: http://localhost:8080/ihrm
```

### Production (Tomcat)
```bash
# Build WAR file
./mvnw clean package

# Deploy to Tomcat
cp target/ihrm-1.0-SNAPSHOT.war $TOMCAT_HOME/webapps/ihrm.war
```

## 🗄️ Database Schema

The database uses MySQL with 18 tables fully normalized to 3NF:

### Main Entities
- **Employee** - Central entity with email, password, personal info
- **SystemRole** - System-wide roles (ADMIN, HR_MANAGER, EMPLOYEE, MANAGER)
- **Department** - Organizational departments
- **DepartmentRole** - Role titles (Director, Manager, Developer, etc.)
- **WorkSchedule** - Employee work schedules by day
- **Attendance** - Daily attendance records
- **Task/EmployeeTask** - Task management with many-to-many assignments
- **Request/RequestResponse** - Request and approval system
- **Contract** - Employment contracts with salary
- **PayrollReceipt** - Payroll transactions
- **Allowance** - Employee allowances and benefits
- **JobListing/Application** - Recruitment management
- **Contact** - Company-wide contact information
- **SystemConfiguration** - Key-value system settings

## 🔐 Security Features

- **Password Security**: SHA-256 hashing with random salt (format: `$SHA256$salt$hash`)
- **Session Management**: HttpSession with configurable timeout
- **Remember Me**: Optional 7-day session extension
- **Access Control**: AuthenticationFilter protects all pages except login
- **SQL Injection Prevention**: Prepared statements throughout
- **Credential Protection**: Database credentials stored in `.env` file (excluded from git)

## 📄 Documentation

For detailed technical documentation and development guidelines, see [CLAUDE.md](CLAUDE.md)

---
Version: 1.0-SNAPSHOT