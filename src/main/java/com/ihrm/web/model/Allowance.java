package com.ihrm.web.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Allowance implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer allowanceId;
    private Integer employeeId;
    private BigDecimal amount;
    private String description;

    public Allowance() {}

    public Allowance(Integer employeeId, BigDecimal amount) {
        this.employeeId = employeeId;
        this.amount = amount;
    }

    public Allowance(Integer employeeId, BigDecimal amount, String description) {
        this.employeeId = employeeId;
        this.amount = amount;
        this.description = description;
    }

    public Integer getAllowanceId() {
        return allowanceId;
    }

    public void setAllowanceId(Integer allowanceId) {
        this.allowanceId = allowanceId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Allowance{" +
                "allowanceId=" + allowanceId +
                ", employeeId=" + employeeId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }
}