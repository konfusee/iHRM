package com.ihrm.web.session;

import com.ihrm.web.dao.SessionDAO;
import com.ihrm.web.model.Session;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class SessionManager {
    private static SessionManager instance;
    private final SessionDAO sessionDAO;
    private static final String SESSION_COOKIE_NAME = "SESSIONID";

    private final int sessionTimeoutMinutes;
    private final int sessionRememberDays;
    private final boolean singleDeviceMode;

    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    private SessionManager() {
        this.sessionDAO = new SessionDAO();

        // Load configuration from environment
        this.sessionTimeoutMinutes = Integer.parseInt(
            getEnvOrDefault("SESSION_TIMEOUT_MINUTES", "30"));
        this.sessionRememberDays = Integer.parseInt(
            getEnvOrDefault("SESSION_REMEMBER_DAYS", "7"));
        this.singleDeviceMode = Boolean.parseBoolean(
            getEnvOrDefault("SESSION_SINGLE_DEVICE", "false"));
    }

    /**
     * Get singleton instance
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Create a new session
     */
    public DatabaseBackedSession createSession(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Integer employeeId,
                                              boolean rememberMe) throws SQLException {

        // Generate secure session ID
        String sessionId = generateSessionId();

        // Create session object
        Session session = new Session(sessionId, employeeId);
        session.setUserAgent(request.getHeader("User-Agent"));
        session.setIpAddress(getClientIpAddress(request));

        // Set expiry time based on remember me option
        LocalDateTime expiresAt;
        if (rememberMe) {
            expiresAt = LocalDateTime.now().plusDays(sessionRememberDays);
        } else {
            expiresAt = LocalDateTime.now().plusMinutes(sessionTimeoutMinutes);
        }
        session.setExpiresAt(expiresAt);

        // If single device mode, deactivate other sessions
        if (singleDeviceMode && employeeId != null) {
            sessionDAO.deactivateOthersForEmployee(employeeId, sessionId);
        }

        // Save session to database
        sessionDAO.create(session);

        // Create cookie
        Cookie cookie = createSessionCookie(sessionId, rememberMe);
        response.addCookie(cookie);

        return new DatabaseBackedSession(session);
    }

    /**
     * Get session from request
     */
    public DatabaseBackedSession getSession(HttpServletRequest request) {
        String sessionId = getSessionIdFromRequest(request);

        if (sessionId == null) {
            return null;
        }

        try {
            Session session = sessionDAO.findById(sessionId);

            if (session == null || !session.isValid()) {
                return null;
            }

            // Update last accessed time
            DatabaseBackedSession dbSession = new DatabaseBackedSession(session);
            dbSession.touch();

            return dbSession;

        } catch (SQLException e) {
            System.err.println("Error retrieving session: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get session from request, create if doesn't exist
     */
    public DatabaseBackedSession getOrCreateSession(HttpServletRequest request,
                                                   HttpServletResponse response) {
        DatabaseBackedSession session = getSession(request);

        if (session == null) {
            try {
                session = createSession(request, response, null, false);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create session", e);
            }
        }

        return session;
    }

    /**
     * Invalidate session
     */
    public void invalidateSession(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = getSessionIdFromRequest(request);

        if (sessionId != null) {
            try {
                sessionDAO.deactivate(sessionId);
            } catch (SQLException e) {
                System.err.println("Error invalidating session: " + e.getMessage());
            }
        }

        // Clear cookie
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * Invalidate all sessions for an employee (logout from all devices)
     */
    public void invalidateAllSessions(Integer employeeId) throws SQLException {
        sessionDAO.deactivateAllForEmployee(employeeId);
    }

    /**
     * Check if session is valid
     */
    public boolean isSessionValid(String sessionId) {
        try {
            return sessionDAO.isSessionValid(sessionId);
        } catch (SQLException e) {
            System.err.println("Error checking session validity: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clean up expired and inactive sessions
     */
    public void cleanupSessions() {
        try {
            int expiredDeleted = sessionDAO.deleteExpired();

            // Delete inactive sessions older than retention period
            int inactiveRetentionDays = Integer.parseInt(
                getEnvOrDefault("SESSION_INACTIVE_RETENTION_DAYS", "30"));
            int inactiveDeleted = sessionDAO.deleteInactive(inactiveRetentionDays);

            System.out.println("Session cleanup: deleted " + expiredDeleted +
                             " expired and " + inactiveDeleted + " inactive sessions");

        } catch (SQLException e) {
            System.err.println("Error during session cleanup: " + e.getMessage());
        }
    }

    /**
     * Generate a secure session ID
     */
    private String generateSessionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Get session ID from request cookies
     */
    private String getSessionIdFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (SESSION_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    /**
     * Create session cookie
     */
    private Cookie createSessionCookie(String sessionId, boolean rememberMe) {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        if (rememberMe) {
            cookie.setMaxAge(sessionRememberDays * 24 * 60 * 60); // Days to seconds
        } else {
            cookie.setMaxAge(-1); // Session cookie
        }

        // Set Secure flag if running in production (HTTPS)
        // cookie.setSecure(true);

        return cookie;
    }

    /**
     * Get client IP address, considering proxies
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // If multiple IPs, take the first one
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }

        return ip;
    }

    /**
     * Get environment variable or default value
     */
    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        value = dotenv.get(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        return defaultValue;
    }

    /**
     * Get session DAO
     */
    public SessionDAO getSessionDAO() {
        return sessionDAO;
    }
}