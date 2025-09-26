package com.ihrm.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PayrollReceipt implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer payrollReceiptId;
    private Integer employeeId;
    private BigDecimal amount;
    private LocalDate date;
    private String description;

    public PayrollReceipt() {}

    public PayrollReceipt(Integer employeeId, BigDecimal amount, LocalDate date) {
        this.employeeId = employeeId;
        this.amount = amount;
        this.date = date;
    }

    public PayrollReceipt(Integer employeeId, BigDecimal amount, LocalDate date, String description) {
        this.employeeId = employeeId;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public Integer getPayrollReceiptId() {
        return payrollReceiptId;
    }

    public void setPayrollReceiptId(Integer payrollReceiptId) {
        this.payrollReceiptId = payrollReceiptId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PayrollReceipt{" +
                "payrollReceiptId=" + payrollReceiptId +
                ", employeeId=" + employeeId +
                ", amount=" + amount +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}