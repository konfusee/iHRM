package com.ihrm.web.dao;

import com.ihrm.web.model.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDAO {

    /**
     * Create a new session in the database
     */
    public Session create(Session session) throws SQLException {
        String sql = "INSERT INTO Session (SessionId, EmployeeId, Active, CreatedAt, " +
                     "LastAccessedAt, ExpiresAt, UserAgent, IpAddress) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, session.getSessionId());

            if (session.getEmployeeId() != null) {
                stmt.setInt(2, session.getEmployeeId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setBoolean(3, session.isActive());
            stmt.setTimestamp(4, Timestamp.valueOf(session.getCreatedAt()));
            stmt.setTimestamp(5, Timestamp.valueOf(session.getLastAccessedAt()));
            stmt.setTimestamp(6, Timestamp.valueOf(session.getExpiresAt()));

            if (session.getUserAgent() != null) {
                stmt.setString(7, session.getUserAgent());
            } else {
                stmt.setNull(7, Types.VARCHAR);
            }

            if (session.getIpAddress() != null) {
                stmt.setString(8, session.getIpAddress());
            } else {
                stmt.setNull(8, Types.VARCHAR);
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating session failed, no rows affected.");
            }

            return session;
        }
    }

    /**
     * Find a session by its ID
     */
    public Session findById(String sessionId) throws SQLException {
        String sql = "SELECT * FROM Session WHERE SessionId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sessionId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToSession(rs);
            }

            return null;
        }
    }

    /**
     * Find all active sessions for an employee
     */
    public List<Session> findActiveByEmployeeId(Integer employeeId) throws SQLException {
        String sql = "SELECT * FROM Session WHERE EmployeeId = ? AND Active = TRUE " +
                     "AND ExpiresAt > NOW() ORDER BY LastAccessedAt DESC";

        List<Session> sessions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sessions.add(mapResultSetToSession(rs));
            }
        }

        return sessions;
    }

    /**
     * Update a session (mainly for updating last accessed time)
     */
    public void update(Session session) throws SQLException {
        String sql = "UPDATE Session SET LastAccessedAt = ?, ExpiresAt = ? " +
                     "WHERE SessionId = ? AND Active = TRUE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(session.getLastAccessedAt()));
            stmt.setTimestamp(2, Timestamp.valueOf(session.getExpiresAt()));
            stmt.setString(3, session.getSessionId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating session failed, no active session found.");
            }
        }
    }

    /**
     * Deactivate a specific session
     */
    public void deactivate(String sessionId) throws SQLException {
        String sql = "UPDATE Session SET Active = FALSE WHERE SessionId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sessionId);
            stmt.executeUpdate();
        }
    }

    /**
     * Deactivate all sessions for an employee (logout from all devices)
     */
    public void deactivateAllForEmployee(Integer employeeId) throws SQLException {
        String sql = "UPDATE Session SET Active = FALSE WHERE EmployeeId = ? AND Active = TRUE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            stmt.executeUpdate();
        }
    }

    /**
     * Deactivate all other sessions for an employee (single device login)
     */
    public void deactivateOthersForEmployee(Integer employeeId, String currentSessionId) throws SQLException {
        String sql = "UPDATE Session SET Active = FALSE WHERE EmployeeId = ? " +
                     "AND SessionId != ? AND Active = TRUE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            stmt.setString(2, currentSessionId);
            stmt.executeUpdate();
        }
    }

    /**
     * Delete expired sessions
     */
    public int deleteExpired() throws SQLException {
        String sql = "DELETE FROM Session WHERE ExpiresAt < NOW()";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            return stmt.executeUpdate();
        }
    }

    /**
     * Delete inactive sessions older than retention period
     */
    public int deleteInactive(int retentionDays) throws SQLException {
        String sql = "DELETE FROM Session WHERE Active = FALSE " +
                     "AND LastAccessedAt < DATE_SUB(NOW(), INTERVAL ? DAY)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, retentionDays);
            return stmt.executeUpdate();
        }
    }

    /**
     * Check if a session exists and is active
     */
    public boolean isSessionValid(String sessionId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Session WHERE SessionId = ? " +
                     "AND Active = TRUE AND ExpiresAt > NOW()";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sessionId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }

        return false;
    }

    /**
     * Update session's last accessed time
     */
    public void updateLastAccessedTime(String sessionId) throws SQLException {
        String sql = "UPDATE Session SET LastAccessedAt = NOW() " +
                     "WHERE SessionId = ? AND Active = TRUE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sessionId);
            stmt.executeUpdate();
        }
    }

    /**
     * Count active sessions for an employee
     */
    public int countActiveSessionsForEmployee(Integer employeeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Session WHERE EmployeeId = ? " +
                     "AND Active = TRUE AND ExpiresAt > NOW()";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    /**
     * Map ResultSet to Session object
     */
    private Session mapResultSetToSession(ResultSet rs) throws SQLException {
        Session session = new Session();

        session.setSessionId(rs.getString("SessionId"));

        Integer employeeId = rs.getInt("EmployeeId");
        if (!rs.wasNull()) {
            session.setEmployeeId(employeeId);
        }

        // No more Data column to parse

        session.setActive(rs.getBoolean("Active"));

        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            session.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp lastAccessedAt = rs.getTimestamp("LastAccessedAt");
        if (lastAccessedAt != null) {
            session.setLastAccessedAt(lastAccessedAt.toLocalDateTime());
        }

        Timestamp expiresAt = rs.getTimestamp("ExpiresAt");
        if (expiresAt != null) {
            session.setExpiresAt(expiresAt.toLocalDateTime());
        }

        session.setUserAgent(rs.getString("UserAgent"));
        session.setIpAddress(rs.getString("IpAddress"));

        return session;
    }
}