package com.ihrm.web.session;

import com.ihrm.web.dao.SessionDAO;
import com.ihrm.web.model.Session;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Read-only wrapper class that provides session interface backed by database
 */
public class DatabaseBackedSession {
    private Session session;
    private SessionDAO sessionDAO;

    public DatabaseBackedSession(Session session) {
        this.session = session;
        this.sessionDAO = new SessionDAO();
    }

    /**
     * Get the session ID
     */
    public String getId() {
        return session.getSessionId();
    }

    /**
     * Get the employee ID associated with this session
     */
    public Integer getEmployeeId() {
        return session.getEmployeeId();
    }

    /**
     * Invalidate the session (deactivate it)
     */
    public void invalidate() {
        try {
            sessionDAO.deactivate(session.getSessionId());
            session.setActive(false);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to invalidate session", e);
        }
    }

    /**
     * Check if session is valid
     */
    public boolean isValid() {
        return session.isValid();
    }

    /**
     * Check if session is active
     */
    public boolean isActive() {
        return session.isActive();
    }

    /**
     * Update last accessed time
     */
    public void touch() {
        session.touch();
        try {
            sessionDAO.updateLastAccessedTime(session.getSessionId());
        } catch (SQLException e) {
            // Log error but don't throw - we don't want to break the request
            System.err.println("Failed to update session last accessed time: " + e.getMessage());
        }
    }

    /**
     * Get creation time
     */
    public LocalDateTime getCreatedAt() {
        return session.getCreatedAt();
    }

    /**
     * Get last accessed time
     */
    public LocalDateTime getLastAccessedAt() {
        return session.getLastAccessedAt();
    }

    /**
     * Get expiry time
     */
    public LocalDateTime getExpiresAt() {
        return session.getExpiresAt();
    }

    /**
     * Get user agent
     */
    public String getUserAgent() {
        return session.getUserAgent();
    }

    /**
     * Get IP address
     */
    public String getIpAddress() {
        return session.getIpAddress();
    }

    /**
     * Get the underlying Session object
     */
    public Session getSession() {
        return session;
    }
}