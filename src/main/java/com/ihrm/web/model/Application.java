package com.ihrm.web.model;

import java.io.Serializable;

public class Application implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer applicationId;
    private Integer jobListingId;
    private Integer employeeId;
    private String name;
    private String cvFile;
    private String address;
    private String idCardImage;
    private String status;

    public Application() {}

    public Application(Integer jobListingId, String name) {
        this.jobListingId = jobListingId;
        this.name = name;
    }

    public Application(Integer jobListingId, String name, String cvFile, String address,
                      String idCardImage, String status) {
        this.jobListingId = jobListingId;
        this.name = name;
        this.cvFile = cvFile;
        this.address = address;
        this.idCardImage = idCardImage;
        this.status = status;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCvFile() {
        return cvFile;
    }

    public void setCvFile(String cvFile) {
        this.cvFile = cvFile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdCardImage() {
        return idCardImage;
    }

    public void setIdCardImage(String idCardImage) {
        this.idCardImage = idCardImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Application{" +
                "applicationId=" + applicationId +
                ", jobListingId=" + jobListingId +
                ", employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", cvFile='" + cvFile + '\'' +
                ", address='" + address + '\'' +
                ", idCardImage='" + idCardImage + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}