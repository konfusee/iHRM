package com.ihrm.web.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer taskId;
    private String taskStatus;
    private LocalDateTime creationTime;
    private LocalDateTime deadlineTime;

    public Task() {}

    public Task(String taskStatus, LocalDateTime creationTime, LocalDateTime deadlineTime) {
        this.taskStatus = taskStatus;
        this.creationTime = creationTime;
        this.deadlineTime = deadlineTime;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(LocalDateTime deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskStatus='" + taskStatus + '\'' +
                ", creationTime=" + creationTime +
                ", deadlineTime=" + deadlineTime +
                '}';
    }
}