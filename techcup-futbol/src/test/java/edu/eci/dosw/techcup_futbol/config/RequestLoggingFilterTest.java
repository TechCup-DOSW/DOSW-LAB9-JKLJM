package edu.eci.dosw.techcup_futbol.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RequestLoggingFilterTest {

    @Test
    void shouldKeepIncomingCorrelationId() throws Exception {
        RequestLoggingFilter filter = new RequestLoggingFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");
        request.addHeader("X-Correlation-Id", "corr-123");
        request.setRemoteAddr("127.0.0.1");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilterInternal(request, response, chain);

        assertEquals("corr-123", response.getHeader("X-Correlation-Id"));
    }

    @Test
    void shouldGenerateCorrelationIdWhenMissing() throws Exception {
        RequestLoggingFilter filter = new RequestLoggingFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/test");
        request.setQueryString("q=1");
        request.setRemoteAddr("127.0.0.1");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilterInternal(request, response, chain);

        String correlationId = response.getHeader("X-Correlation-Id");
        assertFalse(correlationId == null || correlationId.isBlank());
    }
}
