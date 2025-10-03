package com.ihrm.web.model;

import java.time.LocalDateTime;

public class Session {
    private String sessionId;
    private Integer employeeId;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime expiresAt;
    private String userAgent;
    private String ipAddress;

    // Default constructor
    public Session() {
        this.active = true;
    }

    // Constructor for creating new sessions
    public Session(String sessionId, Integer employeeId) {
        this.sessionId = sessionId;
        this.employeeId = employeeId;
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.lastAccessedAt = LocalDateTime.now();
    }

    // Check if session is valid (active and not expired)
    public boolean isValid() {
        return active && LocalDateTime.now().isBefore(expiresAt);
    }

    // Update last accessed time
    public void touch() {
        this.lastAccessedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", employeeId=" + employeeId +
                ", active=" + active +
                ", createdAt=" + createdAt +
                ", lastAccessedAt=" + lastAccessedAt +
                ", expiresAt=" + expiresAt +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}