<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    if (session.getAttribute("employeeId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>iHRM - Dashboard</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;
            background-color: #f7fafc;
            min-height: 100vh;
        }

        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 1rem 0;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
        }

        .header-content {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 2rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            font-size: 1.5rem;
            font-weight: 700;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 2rem;
        }

        .user-name {
            font-size: 0.95rem;
        }

        .btn-logout {
            background-color: rgba(255, 255, 255, 0.2);
            color: white;
            border: 1px solid rgba(255, 255, 255, 0.3);
            padding: 0.5rem 1rem;
            border-radius: 6px;
            text-decoration: none;
            font-size: 0.875rem;
            transition: background-color 0.2s;
        }

        .btn-logout:hover {
            background-color: rgba(255, 255, 255, 0.3);
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 2rem;
        }

        .welcome-section {
            background: white;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
        }

        .welcome-title {
            font-size: 2rem;
            color: #1a202c;
            margin-bottom: 0.5rem;
        }

        .welcome-subtitle {
            color: #718096;
            font-size: 1rem;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;
        }

        .stat-card {
            background: white;
            padding: 1.5rem;
            border-radius: 12px;
            box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
            border-left: 4px solid #667eea;
        }

        .stat-label {
            color: #718096;
            font-size: 0.875rem;
            margin-bottom: 0.5rem;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }

        .stat-value {
            color: #1a202c;
            font-size: 2rem;
            font-weight: 700;
        }

        .quick-actions {
            background: white;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
        }

        .section-title {
            font-size: 1.25rem;
            color: #1a202c;
            margin-bottom: 1.5rem;
            font-weight: 600;
        }

        .action-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 1rem;
        }

        .action-card {
            padding: 1.5rem;
            background: #f7fafc;
            border-radius: 8px;
            text-align: center;
            text-decoration: none;
            color: #4a5568;
            transition: all 0.2s;
            border: 2px solid transparent;
        }

        .action-card:hover {
            background: white;
            border-color: #667eea;
            transform: translateY(-2px);
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
        }

        .action-icon {
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }

        .action-title {
            font-size: 1rem;
            font-weight: 600;
            color: #1a202c;
        }

        .info-table {
            background: white;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
            margin-top: 2rem;
        }

        .info-row {
            display: flex;
            padding: 0.75rem 0;
            border-bottom: 1px solid #e2e8f0;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            flex: 0 0 200px;
            color: #718096;
            font-weight: 600;
        }

        .info-value {
            color: #1a202c;
        }
    </style>
</head>
<body>
    <header class="header">
        <div class="header-content">
            <div class="logo">iHRM System</div>
            <div class="user-info">
                <span class="user-name">Welcome, ${sessionScope.employeeName}</span>
                <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
            </div>
        </div>
    </header>

    <div class="container">
        <div class="welcome-section">
            <h1 class="welcome-title">Welcome back, ${sessionScope.employeeName}!</h1>
            <p class="welcome-subtitle">Here's your dashboard overview</p>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-label">Employee ID</div>
                <div class="stat-value">#${sessionScope.employeeId}</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Department</div>
                <div class="stat-value">
                    <c:choose>
                        <c:when test="${not empty sessionScope.departmentId}">
                            ID: ${sessionScope.departmentId}
                        </c:when>
                        <c:otherwise>
                            Not Assigned
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-label">System Role</div>
                <div class="stat-value">
                    <c:choose>
                        <c:when test="${sessionScope.systemRoleId == 1}">Admin</c:when>
                        <c:when test="${sessionScope.systemRoleId == 2}">HR Manager</c:when>
                        <c:when test="${sessionScope.systemRoleId == 3}">Employee</c:when>
                        <c:when test="${sessionScope.systemRoleId == 4}">Manager</c:when>
                        <c:otherwise>Role ${sessionScope.systemRoleId}</c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="quick-actions">
            <h2 class="section-title">Quick Actions</h2>
            <div class="action-grid">
                <a href="#" class="action-card">
                    <div class="action-icon">📊</div>
                    <div class="action-title">View Attendance</div>
                </a>
                <a href="#" class="action-card">
                    <div class="action-icon">📋</div>
                    <div class="action-title">My Tasks</div>
                </a>
                <a href="#" class="action-card">
                    <div class="action-icon">📝</div>
                    <div class="action-title">Submit Request</div>
                </a>
                <a href="#" class="action-card">
                    <div class="action-icon">💰</div>
                    <div class="action-title">Payroll</div>
                </a>
                <a href="#" class="action-card">
                    <div class="action-icon">📅</div>
                    <div class="action-title">Schedule</div>
                </a>
                <a href="#" class="action-card">
                    <div class="action-icon">👤</div>
                    <div class="action-title">My Profile</div>
                </a>
            </div>
        </div>

        <div class="info-table">
            <h2 class="section-title">Account Information</h2>
            <div class="info-row">
                <div class="info-label">Email Address</div>
                <div class="info-value">${sessionScope.employeeEmail}</div>
            </div>
            <div class="info-row">
                <div class="info-label">Employee ID</div>
                <div class="info-value">${sessionScope.employeeId}</div>
            </div>
            <div class="info-row">
                <div class="info-label">Department ID</div>
                <div class="info-value">
                    <c:choose>
                        <c:when test="${not empty sessionScope.departmentId}">
                            ${sessionScope.departmentId}
                        </c:when>
                        <c:otherwise>
                            Not Assigned
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="info-row">
                <div class="info-label">Department Role ID</div>
                <div class="info-value">
                    <c:choose>
                        <c:when test="${not empty sessionScope.departmentRoleId}">
                            ${sessionScope.departmentRoleId}
                        </c:when>
                        <c:otherwise>
                            Not Assigned
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</body>
</html>