package com.ihrm.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    private List<String> excludedUrls;

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
            HttpSession session = httpRequest.getSession(false);

            if (session == null || session.getAttribute("employeeId") == null) {
                session = httpRequest.getSession(true);
                String requestUrl = httpRequest.getRequestURI();
                if (httpRequest.getQueryString() != null) {
                    requestUrl += "?" + httpRequest.getQueryString();
                }
                session.setAttribute("redirectUrl", requestUrl);
                httpResponse.sendRedirect(contextPath + "/login");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}