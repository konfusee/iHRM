package com.ihrm.web.controller.session;

import com.ihrm.web.dao.SessionDAO;
import com.ihrm.web.model.Session;
import com.ihrm.web.session.DatabaseBackedSession;
import com.ihrm.web.session.SessionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet for displaying all active sessions for the current user
 */
@WebServlet(name = "SessionListServlet", value = "/sessions")
public class SessionListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SessionManager sessionManager = SessionManager.getInstance();
        DatabaseBackedSession currentSession = sessionManager.getSession(request);

        if (currentSession == null || currentSession.getEmployeeId() == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer employeeId = currentSession.getEmployeeId();

        try {
            SessionDAO sessionDAO = sessionManager.getSessionDAO();
            List<Session> activeSessions = sessionDAO.findActiveByEmployeeId(employeeId);

            // Mark the current session
            String currentSessionId = currentSession.getId();
            for (Session session : activeSessions) {
                if (session.getSessionId().equals(currentSessionId)) {
                    request.setAttribute("currentSessionId", currentSessionId);
                    break;
                }
            }

            request.setAttribute("sessions", activeSessions);
            request.setAttribute("sessionCount", activeSessions.size());
            request.getRequestDispatcher("/view/session/list.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load sessions.");
            request.getRequestDispatcher("/view/session/list.jsp").forward(request, response);
        }
    }
}