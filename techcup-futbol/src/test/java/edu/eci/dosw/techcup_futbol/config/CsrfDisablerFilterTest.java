package edu.eci.dosw.techcup_futbol.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertNull;

class CsrfDisablerFilterTest {

    @Test
    void shouldDisableCsrfForRegisterPost() throws Exception {
        CsrfDisablerFilter filter = new CsrfDisablerFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/register");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertNull(request.getAttribute("org.springframework.security.csrf.CsrfToken"));
    }

    @Test
    void shouldNotDisableCsrfForOtherRequests() throws Exception {
        CsrfDisablerFilter filter = new CsrfDisablerFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/register");
        request.setAttribute("org.springframework.security.csrf.CsrfToken", "token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        Object attribute = request.getAttribute("org.springframework.security.csrf.CsrfToken");
        org.junit.jupiter.api.Assertions.assertEquals("token", attribute);
    }
}
