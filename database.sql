-- =============================================
-- iHRM Database Creation Script
-- Human Resource Management System
-- =============================================

-- Drop database if exists and create new one
DROP DATABASE IF EXISTS iHRM;
CREATE DATABASE iHRM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE iHRM;

-- =============================================
-- STEP 1: ERD TEXT FORMAT REPRESENTATION
-- =============================================
/*
ENTITIES AND ATTRIBUTES:
1. Employee (EmployeeId, Email, Password, Name, DateOfBirth)
2. SystemRole (SystemRoleId, Code)
3. Department (DepartmentId, Name)
4. DepartmentRole (DepartmentRoleId, Name)
5. WorkSchedule (WorkScheduleId, DayOfWeek, StartTime, EndTime, EffectiveStartTime, EffectiveEndTime)
6. Attendance (AttendanceId, Date, CheckInTime, CheckOutTime)
7. Task (TaskId, TaskStatus, CreationTime, DeadlineTime)
8. TaskResponse (TaskResponseId, Message, CreationTime)
9. Request (RequestId, RequestStatus, CreationTime)
10. RequestResponse (RequestResponseId, Message, CreationTime)
11. PayrollReceipt (PayrollReceiptId, Amount, Date, Description)
12. Application (ApplicationId, Name, CVFile, Address, IDCardImage, Status)
13. JobListing (JobListingId, Description)
14. Contract (ContractId, StartDate, EndDate, BaseSalary)
15. Contact (ContactId, Title, Email, PhoneNumber, Address)
16. Allowance (AllowanceId, Amount, Description)

RELATIONSHIPS:
One-to-One:
- Employee ↔ SystemRole (Each employee has exactly one system role)

One-to-Many:
- Employee → WorkSchedule (One employee has many schedules)
- Employee → Attendance (One employee has many attendance records)
- Employee → TaskResponse (One employee creates many task responses)
- Employee → Request (One employee raises many requests)
- Employee → RequestResponse (One employee creates many request responses)
- Employee → PayrollReceipt (One employee receives many payroll receipts)
- Employee → Contract (One employee has many contracts over time)
- Employee → Contact (One employee manages many contacts)
- Employee → Allowance (One employee has many allowances)
- Employee → Application (One employee screens/interviews many applications)
- Employee → JobListing (One employee manages many job listings)
- Employee → Department (Many employees work for one department)
- Employee → DepartmentRole (Many employees can have the same department role)
- Department → DepartmentRole (One department has many roles)
- Request → RequestResponse (One request has many responses)
- Task → TaskResponse (One task has many responses)
- JobListing → Application (One job listing has many applications)

Many-to-Many:
- Employee ↔ Task (Employees are assigned to multiple tasks, tasks assigned to multiple employees)
*/

-- =============================================
-- STEP 2: NORMALIZATION ANALYSIS
-- =============================================
/*
The schema is already in 3NF (Third Normal Form) with the following considerations:
1. All tables have primary keys (1NF)
2. No partial dependencies exist (2NF)
3. No transitive dependencies exist (3NF)

Additional normalization decisions:
- Employee-Task relationship requires a junction table (EmployeeTask)
- All foreign key relationships are properly defined
- Separate lookup tables for SystemRole, Department, and DepartmentRole
*/

-- =============================================
-- STEP 3: DATABASE STRUCTURE
-- =============================================

-- Create SystemRole table (Referenced by Employee)
CREATE TABLE SystemRole (
                            SystemRoleId INT PRIMARY KEY AUTO_INCREMENT,
                            Code VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- Create Department table (Referenced by DepartmentRole and Employee)
CREATE TABLE Department (
                            DepartmentId INT PRIMARY KEY AUTO_INCREMENT,
                            Name VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- Create DepartmentRole table (Static - no relationship to Department)
CREATE TABLE DepartmentRole (
                                DepartmentRoleId INT PRIMARY KEY AUTO_INCREMENT,
                                Name VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- Create Employee table (Central entity)
CREATE TABLE Employee (
                          EmployeeId INT PRIMARY KEY AUTO_INCREMENT,
                          Email VARCHAR(255) NOT NULL UNIQUE,
                          Password VARCHAR(255) NOT NULL,
                          Name VARCHAR(100) NOT NULL,
                          DateOfBirth DATE NOT NULL,
                          SystemRoleId INT NOT NULL,
                          DepartmentId INT,
                          DepartmentRoleId INT,
                          PayrollAccount VARCHAR(100),
                          PayrollBank VARCHAR(100),
                          FOREIGN KEY (SystemRoleId) REFERENCES SystemRole(SystemRoleId) ON DELETE RESTRICT ON UPDATE CASCADE,
                          FOREIGN KEY (DepartmentId) REFERENCES Department(DepartmentId) ON DELETE SET NULL ON UPDATE CASCADE,
                          FOREIGN KEY (DepartmentRoleId) REFERENCES DepartmentRole(DepartmentRoleId) ON DELETE SET NULL ON UPDATE CASCADE,
                          INDEX idx_email (Email),
                          INDEX idx_system_role (SystemRoleId),
                          INDEX idx_department (DepartmentId),
                          INDEX idx_dept_role (DepartmentRoleId)
) ENGINE=InnoDB;

-- Create Session table for database-backed sessions
CREATE TABLE Session (
    SessionId VARCHAR(64) PRIMARY KEY,
    EmployeeId INT,
    Active BOOLEAN DEFAULT TRUE,
    CreatedAt DATETIME NOT NULL,
    LastAccessedAt DATETIME NOT NULL,
    ExpiresAt DATETIME NOT NULL,
    UserAgent VARCHAR(500),
    IpAddress VARCHAR(45),
    FOREIGN KEY (EmployeeId) REFERENCES Employee(EmployeeId) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_employee (EmployeeId),
    INDEX idx_expires (ExpiresAt),
    INDEX idx_active (Active),
    INDEX idx_employee_active (EmployeeId, Active)
) ENGINE=InnoDB;

-- Create WorkSchedule table
CREATE TABLE WorkSchedule (
                              WorkScheduleId INT PRIMARY KEY AUTO_INCREMENT,
                              EmployeeId INT NOT NULL,
                              DayOfWeek VARCHAR(20) NOT NULL,
                              StartTime TIME NOT NULL,
                              EndTime TIME NOT NULL,
                              EffectiveStartTime TIME NOT NULL,
                              EffectiveEndTime TIME NOT NULL,
                              FOREIGN KEY (EmployeeId) REFERENCES Employee(EmployeeId) ON DELETE CASCADE ON UPDATE CASCADE,
                              INDEX idx_employee (EmployeeId)
) ENGINE=InnoDB;

-- Create Attendance table
CREATE TABLE Attendance (
                            AttendanceId INT PRIMARY KEY AUTO_INCREMENT,
                            EmployeeId INT NOT NULL,
                            Date DATE NOT NULL,
                            CheckInTime TIME,
                            CheckOutTime TIME,
                            FOREIGN KEY (EmployeeId) REFERENCES Employee(EmployeeId) ON DELETE CASCADE ON UPDATE CASCADE,
                            INDEX idx_employee (EmployeeId),
                            INDEX idx_date (Date)
) ENGINE=InnoDB;

-- Create Task table
CREATE TABLE Task (
                      TaskId INT PRIMARY KEY AUTO_INCREMENT,
                      TaskStatus VARCHAR(50),
                      CreationTime DATETIME,
                      DeadlineTime DATETIME,
                      INDEX idx_status (TaskStatus)
) ENGINE=InnoDB;

-- Create Junction table for Employee-Task Many-to-Many relationship
CREATE TABLE EmployeeTask (
                              EmployeeId INT NOT NULL,
                              TaskId INT NOT NULL,
                              PRIMARY KEY (EmployeeId, TaskId),
                              FOREIGN KEY (EmployeeId) REFERENCES Employee(EmployeeId) ON DELETE CASCADE ON UPDATE CASCADE,
                              FOREIGN KEY (TaskId) REFERENCES Task(TaskId) ON DELETE CASCADE ON UPDATE CASCADE,
                              INDEX idx_employee (EmployeeId),
                              INDEX idx_task (TaskId)
) ENGINE=InnoDB;

-- Create TaskResponse table
CREATE TABLE TaskResponse (
                              TaskResponseId INT PRIMARY KEY AUTO_INCREMENT,
                              TaskId INT NOT NULL,
                              EmployeeId INT NOT NULL,
                              Message TEXT,
                              CreationTime DATETIME,
                              FOREIGN KEY (TaskId) REFERENCES Task(TaskId) ON DELETE CASCADE ON UPDATE CASCADE,
                              FOREIGN KEY (EmployeeId) REFERENCES Employee(EmployeeId) ON DELETE CASCADE ON UPDATE CASCADE,
                              INDEX idx_task (TaskId),
                              INDEX idx_employee (EmployeeId)
) ENGINE=InnoDB;

-- Create Request table
CREATE TABLE Request (
                         RequestId INT PRIMARY KEY AUTO_INCREMENT,
                         EmployeeId INT NOT NULL,
                         RequestStatus VARCHAR(50),
                         CreationTime DATETIME,
                         FOREIGN KEY (EmployeeId) REFERENCES Employee(EmployeeId) ON DELETE CASCADE ON UPDATE CASCADE,
                         INDEX idx_employee (EmployeeId)
) ENGINE=InnoDB;

-- Create RequestResponse table
CREATE TABLE RequestResponse (
                                 RequestResponseId INT PRIMARY KEY AUTO_INCREMENT,
                                 RequestId INT NOT NULL,
                                 EmployeeId INT NOT NULL,
                                 Message TEXT,
                                 CreationTime DATETIME,
                                 FOREIGN KEY (RequestId) REFERENCES Request(RequestId) ON DELETE CASCADE ON UPDATE CASCADE,
                                 FOREIGN KEY (EmployeeId) REFERENCES Employee(EmployeeId) ON DELETE CASCADE ON UPDATE CASCADE,
                                 INDEX idx_request (RequestId),
                                 INDEX idx_employee (EmployeeId)
) ENGINE=InnoDB;

-- Create Contract table
CREATE TABLE Contract (
                          ContractId INT PRIMARY KEY AUTO_INCREMENT,
                          EmployeeId INT NOT NULL,
                          StartDate DATE NOT NULL,
                          EndDate DATE,
                          BaseSalary DECIMAL(12,2) NOT NULL,
                          FOREIGN KEY (EmployeeId) REFERENCES Employee(EmployeeId) ON DELETE CASCADE ON UPDATE CASCADE,
                          INDEX idx_employee (EmployeeId)
) ENGINE=InnoDB;

-- Create PayrollReceipt table
CREATE TABLE PayrollReceipt (
                                PayrollReceiptId INT PRIMARY KEY AUTO_INCREMENT,
                                EmployeeId INT NOT NULL,
                                Amount DECIMAL(12,2) NOT NULL,
                                Date DATE NOT NULL,
                                Description TEXT,
                                FOREIGN KEY (EmployeeId) REFERENCES Employee(EmployeeId) ON DELETE CASCADE ON UPDATE CASCADE,
                                INDEX idx_employee (EmployeeId),
                                INDEX idx_date (Date)
) ENGINE=InnoDB;

-- Create Allowance table
CREATE TABLE Allowance (
                           AllowanceId INT PRIMARY KEY AUTO_INCREMENT,
                           EmployeeId INT NOT NULL,
                           Amount DECIMAL(10,2) NOT NULL,
                           Description TEXT,
                           FOREIGN KEY (EmployeeId) REFERENCES Employee(EmployeeId) ON DELETE CASCADE ON UPDATE CASCADE,
                           INDEX idx_employee (EmployeeId)
) ENGINE=InnoDB;

-- Create Contact table (Static - system contact information)
CREATE TABLE Contact (
                         ContactId INT PRIMARY KEY AUTO_INCREMENT,
                         Title VARCHAR(100) NOT NULL,
                         Email VARCHAR(255),
                         PhoneNumber VARCHAR(20),
                         Address TEXT
) ENGINE=InnoDB;

-- Create SystemConfiguration table (Static - system settings)
CREATE TABLE SystemConfiguration (
                                     ConfigKey VARCHAR(100) PRIMARY KEY,
                                     ConfigValue TEXT
) ENGINE=InnoDB;

-- Create JobListing table
CREATE TABLE JobListing (
                            JobListingId INT PRIMARY KEY AUTO_INCREMENT,
                            EmployeeId INT,
                            Description TEXT NOT NULL,
                            FOREIGN KEY (EmployeeId) REFERENCES Employee(EmployeeId) ON DELETE SET NULL ON UPDATE CASCADE,
                            INDEX idx_employee (EmployeeId)
) ENGINE=InnoDB;

-- Create Application table
CREATE TABLE Application (
                             ApplicationId INT PRIMARY KEY AUTO_INCREMENT,
                             JobListingId INT NOT NULL,
                             EmployeeId INT,
                             Name VARCHAR(100) NOT NULL,
                             CVFile VARCHAR(500),
                             Address TEXT,
                             IDCardImage VARCHAR(500),
                             Status VARCHAR(50),
                             FOREIGN KEY (JobListingId) REFERENCES JobListing(JobListingId) ON DELETE CASCADE ON UPDATE CASCADE,
                             FOREIGN KEY (EmployeeId) REFERENCES Employee(EmployeeId) ON DELETE SET NULL ON UPDATE CASCADE,
                             INDEX idx_job_listing (JobListingId),
                             INDEX idx_employee (EmployeeId)
) ENGINE=InnoDB;

-- =============================================
-- STEP 4: SAMPLE DATA INSERTION
-- =============================================

-- Insert sample SystemRoles
INSERT INTO SystemRole (Code) VALUES
                                  ('ADMIN'),
                                  ('HR_MANAGER'),
                                  ('EMPLOYEE'),
                                  ('MANAGER');

-- Insert sample Departments
INSERT INTO Department (Name) VALUES
                                  ('Human Resources'),
                                  ('Information Technology'),
                                  ('Finance'),
                                  ('Marketing'),
                                  ('Operations');

-- Insert sample DepartmentRoles (Static - no department relationship)
INSERT INTO DepartmentRole (Name) VALUES
                                      ('Director'),
                                      ('Manager'),
                                      ('Specialist'),
                                      ('Senior Developer'),
                                      ('Developer'),
                                      ('Accountant'),
                                      ('HR Officer'),
                                      ('Administrative Assistant');

-- Insert sample Contacts (Static - company contacts)
INSERT INTO Contact (Title, Email, PhoneNumber, Address) VALUES
                                                             ('Main Office', 'info@company.com', '+84-24-1234-5678', '123 Main St, Hanoi'),
                                                             ('HR Department', 'hr@company.com', '+84-24-1234-5679', '123 Main St, Hanoi'),
                                                             ('Emergency Hotline', 'emergency@company.com', '+84-900-123-456', '24/7 Support');

-- Insert sample SystemConfiguration
INSERT INTO SystemConfiguration (ConfigKey, ConfigValue) VALUES
                                                             ('company_name', 'iHRM Company Ltd.'),
                                                             ('default_working_hours', '8'),
                                                             ('max_leave_days_per_year', '12'),
                                                             ('probation_period_months', '2'),
                                                             ('password_min_length', '8'),
                                                             ('session_timeout_minutes', '30'),
                                                             ('date_format', 'DD/MM/YYYY'),
                                                             ('currency', 'VND');

-- =============================================
-- DATABASE DOCUMENTATION
-- =============================================
/*
Database: iHRM
Purpose: Integrated Human Resource Management System
Version: 1.0

Key Features:
1. Employee Management with role-based access
2. Attendance tracking and work scheduling
3. Task management with multi-employee assignments (via junction table)
4. Request and response handling
5. Payroll and allowance management
6. Recruitment (job listings and applications)
7. Static contact information table
8. Contract management
9. System configuration storage

Normalized Structure:
- All tables in 3NF
- Junction table EmployeeTask for many-to-many relationship
- Static tables: Contact, DepartmentRole, SystemConfiguration
- Proper foreign key constraints
- Indexes on foreign keys for performance

Notes:
- DepartmentRole is now independent of Department
- Contact table stores company-wide contact information (not employee-specific)
- SystemConfiguration stores key-value pairs for system settings
*/

-- End of iHRM Database Creation Script