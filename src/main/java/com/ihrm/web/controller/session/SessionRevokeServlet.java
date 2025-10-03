package com.ihrm.web.controller.session;

import com.ihrm.web.dao.SessionDAO;
import com.ihrm.web.session.DatabaseBackedSession;
import com.ihrm.web.session.SessionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet for revoking (deactivating) sessions
 */
@WebServlet(name = "SessionRevokeServlet", value = "/session/revoke")
public class SessionRevokeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SessionManager sessionManager = SessionManager.getInstance();
        DatabaseBackedSession currentSession = sessionManager.getSession(request);

        if (currentSession == null || currentSession.getEmployeeId() == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer employeeId = currentSession.getEmployeeId();

        String action = request.getParameter("action");
        String sessionId = request.getParameter("sessionId");

        try {
            SessionDAO sessionDAO = sessionManager.getSessionDAO();

            if ("revokeAll".equals(action)) {
                // Logout from all devices
                sessionDAO.deactivateAllForEmployee(employeeId);
                // Redirect to login since current session is also invalidated
                response.sendRedirect(request.getContextPath() + "/login?success=Logged out from all devices.");

            } else if ("revokeOthers".equals(action)) {
                // Logout from all other devices
                String currentSessionId = currentSession.getId();
                sessionDAO.deactivateOthersForEmployee(employeeId, currentSessionId);
                response.sendRedirect(request.getContextPath() + "/sessions?success=Logged out from all other devices.");

            } else if ("revoke".equals(action) && sessionId != null) {
                // Revoke specific session
                String currentSessionId = currentSession.getId();

                if (sessionId.equals(currentSessionId)) {
                    // Revoking current session - logout
                    sessionDAO.deactivate(sessionId);
                    response.sendRedirect(request.getContextPath() + "/login?success=Session revoked successfully.");
                } else {
                    // Revoking another session
                    // Verify it belongs to the current user for security
                    if (isSessionOwnedByEmployee(sessionDAO, sessionId, employeeId)) {
                        sessionDAO.deactivate(sessionId);
                        response.sendRedirect(request.getContextPath() + "/sessions?success=Session revoked successfully.");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/sessions?error=Invalid session.");
                    }
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/sessions?error=Invalid request.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/sessions?error=Failed to revoke session.");
        }
    }

    /**
     * Check if a session belongs to a specific employee
     */
    private boolean isSessionOwnedByEmployee(SessionDAO sessionDAO, String sessionId, Integer employeeId)
            throws SQLException {
        com.ihrm.web.model.Session session = sessionDAO.findById(sessionId);
        return session != null && employeeId.equals(session.getEmployeeId());
    }
}