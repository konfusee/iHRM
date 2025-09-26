package com.ihrm.web.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TaskResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer taskResponseId;
    private Integer taskId;
    private Integer employeeId;
    private String message;
    private LocalDateTime creationTime;

    public TaskResponse() {}

    public TaskResponse(Integer taskId, Integer employeeId, String message, LocalDateTime creationTime) {
        this.taskId = taskId;
        this.employeeId = employeeId;
        this.message = message;
        this.creationTime = creationTime;
    }

    public Integer getTaskResponseId() {
        return taskResponseId;
    }

    public void setTaskResponseId(Integer taskResponseId) {
        this.taskResponseId = taskResponseId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
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
        return "TaskResponse{" +
                "taskResponseId=" + taskResponseId +
                ", taskId=" + taskId +
                ", employeeId=" + employeeId +
                ", message='" + message + '\'' +
                ", creationTime=" + creationTime +
                '}';
    }
}