package com.ihrm.web.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RequestResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer requestResponseId;
    private Integer requestId;
    private Integer employeeId;
    private String message;
    private LocalDateTime creationTime;

    public RequestResponse() {}

    public RequestResponse(Integer requestId, Integer employeeId, String message, LocalDateTime creationTime) {
        this.requestId = requestId;
        this.employeeId = employeeId;
        this.message = message;
        this.creationTime = creationTime;
    }

    public Integer getRequestResponseId() {
        return requestResponseId;
    }

    public void setRequestResponseId(Integer requestResponseId) {
        this.requestResponseId = requestResponseId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "RequestResponse{" +
                "requestResponseId=" + requestResponseId +
                ", requestId=" + requestId +
                ", employeeId=" + employeeId +
                ", message='" + message + '\'' +
                ", creationTime=" + creationTime +
                '}';
    }
}