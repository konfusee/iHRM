package com.ihrm.web.model;

import java.io.Serializable;
import java.time.LocalTime;

public class WorkSchedule implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer workScheduleId;
    private Integer employeeId;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime effectiveStartTime;
    private LocalTime effectiveEndTime;

    public WorkSchedule() {}

    public WorkSchedule(Integer employeeId, String dayOfWeek, LocalTime startTime, LocalTime endTime,
                       LocalTime effectiveStartTime, LocalTime effectiveEndTime) {
        this.employeeId = employeeId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.effectiveStartTime = effectiveStartTime;
        this.effectiveEndTime = effectiveEndTime;
    }

    public Integer getWorkScheduleId() {
        return workScheduleId;
    }

    public void setWorkScheduleId(Integer workScheduleId) {
        this.workScheduleId = workScheduleId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public void setEffectiveStartTime(LocalTime effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    public LocalTime getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public void setEffectiveEndTime(LocalTime effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    @Override
    public String toString() {
        return "WorkSchedule{" +
                "workScheduleId=" + workScheduleId +
                ", employeeId=" + employeeId +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", effectiveStartTime=" + effectiveStartTime +
                ", effectiveEndTime=" + effectiveEndTime +
                '}';
    }
}