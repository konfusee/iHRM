package com.ihrm.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Contract implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer contractId;
    private Integer employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal baseSalary;

    public Contract() {}

    public Contract(Integer employeeId, LocalDate startDate, BigDecimal baseSalary) {
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.baseSalary = baseSalary;
    }

    public Contract(Integer employeeId, LocalDate startDate, LocalDate endDate, BigDecimal baseSalary) {
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.baseSalary = baseSalary;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "contractId=" + contractId +
                ", employeeId=" + employeeId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", baseSalary=" + baseSalary +
                '}';
    }
}