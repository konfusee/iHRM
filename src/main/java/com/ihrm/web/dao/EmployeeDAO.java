package com.ihrm.web.dao;

import com.ihrm.web.model.Employee;
import com.ihrm.web.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public Employee create(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employee (Email, Password, Name, DateOfBirth, SystemRoleId, " +
                    "DepartmentId, DepartmentRoleId, PayrollAccount, PayrollBank) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, employee.getEmail());
            stmt.setString(2, PasswordUtil.hashPassword(employee.getPassword()));
            stmt.setString(3, employee.getName());
            stmt.setDate(4, Date.valueOf(employee.getDateOfBirth()));
            stmt.setInt(5, employee.getSystemRoleId());

            if (employee.getDepartmentId() != null) {
                stmt.setInt(6, employee.getDepartmentId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            if (employee.getDepartmentRoleId() != null) {
                stmt.setInt(7, employee.getDepartmentRoleId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            if (employee.getPayrollAccount() != null) {
                stmt.setString(8, employee.getPayrollAccount());
            } else {
                stmt.setNull(8, Types.VARCHAR);
            }

            if (employee.getPayrollBank() != null) {
                stmt.setString(9, employee.getPayrollBank());
            } else {
                stmt.setNull(9, Types.VARCHAR);
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    employee.setEmployeeId(rs.getInt(1));
                }
            }
        }
        return employee;
    }

    public Employee findById(Integer employeeId) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE EmployeeId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
        }
        return null;
    }

    public Employee findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE Email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
        }
        return null;
    }

    public Employee authenticate(String email, String password) throws SQLException {
        Employee employee = findByEmail(email);
        if (employee != null && PasswordUtil.verifyPassword(password, employee.getPassword())) {
            return employee;
        }
        return null;
    }

    public boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE Employee SET Email = ?, Name = ?, DateOfBirth = ?, " +
                    "SystemRoleId = ?, DepartmentId = ?, DepartmentRoleId = ?, " +
                    "PayrollAccount = ?, PayrollBank = ? WHERE EmployeeId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getEmail());
            stmt.setString(2, employee.getName());
            stmt.setDate(3, Date.valueOf(employee.getDateOfBirth()));
            stmt.setInt(4, employee.getSystemRoleId());

            if (employee.getDepartmentId() != null) {
                stmt.setInt(5, employee.getDepartmentId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            if (employee.getDepartmentRoleId() != null) {
                stmt.setInt(6, employee.getDepartmentRoleId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            if (employee.getPayrollAccount() != null) {
                stmt.setString(7, employee.getPayrollAccount());
            } else {
                stmt.setNull(7, Types.VARCHAR);
            }

            if (employee.getPayrollBank() != null) {
                stmt.setString(8, employee.getPayrollBank());
            } else {
                stmt.setNull(8, Types.VARCHAR);
            }

            stmt.setInt(9, employee.getEmployeeId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updatePassword(Integer employeeId, String newPassword) throws SQLException {
        String sql = "UPDATE Employee SET Password = ? WHERE EmployeeId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, PasswordUtil.hashPassword(newPassword));
            stmt.setInt(2, employeeId);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(Integer employeeId) throws SQLException {
        String sql = "DELETE FROM Employee WHERE EmployeeId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Employee> findAll() throws SQLException {
        String sql = "SELECT * FROM Employee ORDER BY Name";
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        }
        return employees;
    }

    public List<Employee> findByDepartment(Integer departmentId) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE DepartmentId = ? ORDER BY Name";
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        }
        return employees;
    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setEmployeeId(rs.getInt("EmployeeId"));
        employee.setEmail(rs.getString("Email"));
        employee.setPassword(rs.getString("Password"));
        employee.setName(rs.getString("Name"));

        Date dateOfBirth = rs.getDate("DateOfBirth");
        if (dateOfBirth != null) {
            employee.setDateOfBirth(dateOfBirth.toLocalDate());
        }

        employee.setSystemRoleId(rs.getInt("SystemRoleId"));

        Integer departmentId = rs.getObject("DepartmentId", Integer.class);
        employee.setDepartmentId(departmentId);

        Integer departmentRoleId = rs.getObject("DepartmentRoleId", Integer.class);
        employee.setDepartmentRoleId(departmentRoleId);

        employee.setPayrollAccount(rs.getString("PayrollAccount"));
        employee.setPayrollBank(rs.getString("PayrollBank"));

        return employee;
    }
}