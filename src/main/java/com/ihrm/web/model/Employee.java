package com.ihrm.web.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeeId;
    private String email;
    private String password;
    private String name;
    private LocalDate dateOfBirth;
    private Integer systemRoleId;
    private Integer departmentId;
    private Integer departmentRoleId;
    private String payrollAccount;
    private String payrollBank;

    public Employee() {}

    public Employee(String email, String password, String name, LocalDate dateOfBirth, Integer systemRoleId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.systemRoleId = systemRoleId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getSystemRoleId() {
        return systemRoleId;
    }

    public void setSystemRoleId(Integer systemRoleId) {
        this.systemRoleId = systemRoleId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getDepartmentRoleId() {
        return departmentRoleId;
    }

    public void setDepartmentRoleId(Integer departmentRoleId) {
        this.departmentRoleId = departmentRoleId;
    }

    public String getPayrollAccount() {
        return payrollAccount;
    }

    public void setPayrollAccount(String payrollAccount) {
        this.payrollAccount = payrollAccount;
    }

    public String getPayrollBank() {
        return payrollBank;
    }

    public void setPayrollBank(String payrollBank) {
        this.payrollBank = payrollBank;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", systemRoleId=" + systemRoleId +
                ", departmentId=" + departmentId +
                ", departmentRoleId=" + departmentRoleId +
                ", payrollAccount='" + payrollAccount + '\'' +
                ", payrollBank='" + payrollBank + '\'' +
                '}';
    }
}