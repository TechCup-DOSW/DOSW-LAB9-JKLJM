package edu.eci.dosw.techcup_futbol.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom filter to disable CSRF token requirement for /api/register endpoint.
 * This allows POST requests to the registration endpoint without CSRF tokens,
 * making it easier to test with tools like Postman.
 */
@Component
public class CsrfDisablerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Disable CSRF for POST requests to /api/register
        if ("POST".equals(request.getMethod()) && request.getRequestURI().contains("/api/register")) {
            request.setAttribute("org.springframework.security.csrf.CsrfToken", null);
        }
        filterChain.doFilter(request, response);
    }
}
