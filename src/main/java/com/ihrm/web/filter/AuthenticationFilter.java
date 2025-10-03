package com.ihrm.web.filter;

import com.ihrm.web.dao.EmployeeDAO;
import com.ihrm.web.model.Employee;
import com.ihrm.web.session.DatabaseBackedSession;
import com.ihrm.web.session.SessionManager;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    private List<String> excludedUrls;
    private EmployeeDAO employeeDAO;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        excludedUrls = Arrays.asList(
            "/",
            "",
            "/login",
            "/login.jsp",
            "/index.jsp",
            "/view/index.jsp",
            "/css/",
            "/js/",
            "/images/",
            "/favicon.ico"
        );
        employeeDAO = new EmployeeDAO();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getServletPath();
        String contextPath = httpRequest.getContextPath();

        boolean isExcluded = false;
        for (String excludedUrl : excludedUrls) {
            if (path.equals(excludedUrl) || (excludedUrl.endsWith("/") && path.startsWith(excludedUrl))) {
                isExcluded = true;
                break;
            }
        }

        if (!isExcluded) {
            SessionManager sessionManager = SessionManager.getInstance();
            DatabaseBackedSession session = sessionManager.getSession(httpRequest);

            if (session == null || !session.isValid() || session.getEmployeeId() == null) {
                // Build redirect URL to pass as parameter to login page
                String requestUrl = httpRequest.getRequestURI();
                if (httpRequest.getQueryString() != null) {
                    requestUrl += "?" + httpRequest.getQueryString();
                }
                // Pass the redirect URL as a parameter to the login page
                httpResponse.sendRedirect(contextPath + "/login?redirectUrl=" +
                    java.net.URLEncoder.encode(requestUrl, "UTF-8"));
                return;
            }

            // Update last accessed time
            session.touch();

            // Make session available to the request
            httpRequest.setAttribute("dbSession", session);

            // Fetch fresh employee data from database to avoid staleness
            Integer employeeId = session.getEmployeeId();
            if (employeeId != null) {
                try {
                    Employee employee = employeeDAO.findById(employeeId);
                    if (employee != null) {
                        // Make fresh employee data available to the request
                        httpRequest.setAttribute("employee", employee);
                        httpRequest.setAttribute("employeeId", employee.getEmployeeId());
                        httpRequest.setAttribute("employeeName", employee.getName());
                        httpRequest.setAttribute("employeeEmail", employee.getEmail());
                        httpRequest.setAttribute("systemRoleId", employee.getSystemRoleId());
                        httpRequest.setAttribute("departmentId", employee.getDepartmentId());
                        httpRequest.setAttribute("departmentRoleId", employee.getDepartmentRoleId());
                    } else {
                        // Employee not found in database - invalid session
                        sessionManager.invalidateSession(httpRequest, httpResponse);
                        httpResponse.sendRedirect(contextPath + "/login");
                        return;
                    }
                } catch (SQLException e) {
                    // Log error but don't break the request
                    System.err.println("Failed to fetch employee data: " + e.getMessage());
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}