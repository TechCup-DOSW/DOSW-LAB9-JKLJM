package edu.eci.dosw.techcup_futbol.config;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String CORRELATION_ID_MDC_KEY = "correlationId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String correlationId = resolveCorrelationId(request);
        MDC.put(CORRELATION_ID_MDC_KEY, correlationId);
        response.setHeader(CORRELATION_ID_HEADER, correlationId);

        long start = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String remoteAddr = request.getRemoteAddr();

        String path = query == null ? uri : uri + "?" + query;
        LOGGER.info("HTTP request started method={} path={} remoteIp={}", method, path, remoteAddr);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = System.currentTimeMillis() - start;
            int status = response.getStatus();
            LOGGER.info("HTTP request completed method={} path={} status={} durationMs={}", method, path, status, durationMs);
            MDC.remove(CORRELATION_ID_MDC_KEY);
        }
    }

    private String resolveCorrelationId(HttpServletRequest request) {
        String incomingId = request.getHeader(CORRELATION_ID_HEADER);
        if (incomingId == null || incomingId.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return incomingId;
    }
}
