package com.ihrm.web.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.ihrm.web.dao.EmployeeDAO;
import com.ihrm.web.model.Employee;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        employeeDAO = new EmployeeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            request.getRequestDispatcher("/view/auth/login/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Email and password are required.");
            request.getRequestDispatcher("/view/auth/login/index.jsp").forward(request, response);
            return;
        }

        try {
            Employee employee = employeeDAO.authenticate(email.trim(), password);

            if (employee != null) {
                HttpSession session = request.getSession(true);

                session.setAttribute("employeeId", employee.getEmployeeId());
                session.setAttribute("employeeName", employee.getName());
                session.setAttribute("employeeEmail", employee.getEmail());
                session.setAttribute("systemRoleId", employee.getSystemRoleId());
                session.setAttribute("departmentId", employee.getDepartmentId());
                session.setAttribute("departmentRoleId", employee.getDepartmentRoleId());

                if ("true".equals(remember)) {
                    session.setMaxInactiveInterval(7 * 24 * 60 * 60);
                } else {
                    session.setMaxInactiveInterval(30 * 60);
                }

                String redirectUrl = (String) session.getAttribute("redirectUrl");
                if (redirectUrl != null) {
                    session.removeAttribute("redirectUrl");
                    response.sendRedirect(redirectUrl);
                } else {
                    response.sendRedirect(request.getContextPath() + "/");
                }
            } else {
                request.setAttribute("error", "Invalid email or password. Please try again.");
                request.getRequestDispatcher("/view/auth/login/index.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during login. Please try again later.");
            request.getRequestDispatcher("/view/auth/login/index.jsp").forward(request, response);
        }
    }
}