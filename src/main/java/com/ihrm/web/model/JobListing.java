package com.ihrm.web.model;

import java.io.Serializable;

public class JobListing implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer jobListingId;
    private Integer employeeId;
    private String description;

    public JobListing() {}

    public JobListing(String description) {
        this.description = description;
    }

    public JobListing(Integer employeeId, String description) {
        this.employeeId = employeeId;
        this.description = description;
    }

    public Integer getJobListingId() {
        return jobListingId;
    }

    public void setJobListingId(Integer jobListingId) {
        this.jobListingId = jobListingId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "JobListing{" +
                "jobListingId=" + jobListingId +
                ", employeeId=" + employeeId +
                ", description='" + description + '\'' +
                '}';
    }
}