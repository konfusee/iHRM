package com.ihrm.web.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer requestId;
    private Integer employeeId;
    private String requestStatus;
    private LocalDateTime creationTime;

    public Request() {}

    public Request(Integer employeeId, String requestStatus, LocalDateTime creationTime) {
        this.employeeId = employeeId;
        this.requestStatus = requestStatus;
        this.creationTime = creationTime;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestId=" + requestId +
                ", employeeId=" + employeeId +
                ", requestStatus='" + requestStatus + '\'' +
                ", creationTime=" + creationTime +
                '}';
    }
}