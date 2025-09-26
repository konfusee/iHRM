package com.ihrm.web.model;

import java.io.Serializable;
import java.util.Objects;

public class EmployeeTask implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeeId;
    private Integer taskId;

    public EmployeeTask() {}

    public EmployeeTask(Integer employeeId, Integer taskId) {
        this.employeeId = employeeId;
        this.taskId = taskId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeTask that = (EmployeeTask) o;
        return Objects.equals(employeeId, that.employeeId) &&
               Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, taskId);
    }

    @Override
    public String toString() {
        return "EmployeeTask{" +
                "employeeId=" + employeeId +
                ", taskId=" + taskId +
                '}';
    }
}