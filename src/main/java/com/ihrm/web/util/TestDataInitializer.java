package com.ihrm.web.util;

import com.ihrm.web.dao.EmployeeDAO;
import com.ihrm.web.model.Employee;

import java.sql.SQLException;
import java.time.LocalDate;

public class TestDataInitializer {

    public static void main(String[] args) {
        EmployeeDAO employeeDAO = new EmployeeDAO();

        try {
            // Create a test admin user
            // Note: Passwords are automatically hashed by EmployeeDAO.create()
            Employee admin = new Employee();
            admin.setEmail("admin@ihrm.com");
            admin.setPassword("admin123");  // Will be hashed with SHA-256 + salt
            admin.setName("System Administrator");
            admin.setDateOfBirth(LocalDate.of(1990, 1, 1));
            admin.setSystemRoleId(1); // ADMIN role
            admin.setDepartmentId(1); // HR Department
            admin.setDepartmentRoleId(1); // Director

            // Check if admin already exists
            Employee existingAdmin = employeeDAO.findByEmail("admin@ihrm.com");
            if (existingAdmin == null) {
                employeeDAO.create(admin);
                System.out.println("Admin user created successfully!");
                System.out.println("Email: admin@ihrm.com");
                System.out.println("Password: admin123");
            } else {
                System.out.println("Admin user already exists.");
            }

            // Create a test employee user
            Employee employee = new Employee();
            employee.setEmail("john.doe@ihrm.com");
            employee.setPassword("password123");  // Will be hashed with SHA-256 + salt
            employee.setName("John Doe");
            employee.setDateOfBirth(LocalDate.of(1995, 5, 15));
            employee.setSystemRoleId(3); // EMPLOYEE role
            employee.setDepartmentId(2); // IT Department
            employee.setDepartmentRoleId(5); // Developer

            // Check if employee already exists
            Employee existingEmployee = employeeDAO.findByEmail("john.doe@ihrm.com");
            if (existingEmployee == null) {
                employeeDAO.create(employee);
                System.out.println("\nEmployee user created successfully!");
                System.out.println("Email: john.doe@ihrm.com");
                System.out.println("Password: password123");
            } else {
                System.out.println("Employee user already exists.");
            }

            System.out.println("\n=== Test Data Initialization Complete ===");
            System.out.println("You can now login with the test accounts.");

        } catch (SQLException e) {
            System.err.println("Error initializing test data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}