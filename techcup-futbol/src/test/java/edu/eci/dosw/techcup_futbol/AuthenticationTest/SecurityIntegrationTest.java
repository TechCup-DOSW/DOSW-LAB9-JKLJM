package edu.eci.dosw.techcup_futbol.AuthenticationTest;

import edu.eci.dosw.techcup_futbol.entity.RoleEntity;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.repository.RoleRepository;
import edu.eci.dosw.techcup_futbol.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void seedUser() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilters(springSecurityFilterChain)
            .build();

        userRepository.deleteAll();
        roleRepository.deleteAll();

        RoleEntity adminRole = new RoleEntity();
        adminRole.setName("ADMIN");
        RoleEntity savedRole = roleRepository.save(adminRole);

        UserEntity user = new UserEntity();
        user.setName("Admin");
        user.setEmail("admin@techcup.com");
        user.setPassword(passwordEncoder.encode("admin123"));
        user.setRoles(Set.of(savedRole));

        userRepository.save(user);
    }

    @Test
    void shouldLoginAndReturnJwtToken() throws Exception {
        String body = "{\"username\":\"admin@techcup.com\",\"password\":\"admin123\"}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void shouldRejectLoginWhenCredentialsAreInvalid() throws Exception {
        String body = "{\"username\":\"admin@techcup.com\",\"password\":\"bad-pass\"}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void shouldAllowProtectedEndpointWithValidJwt() throws Exception {
        String token = loginAndExtractToken("admin@techcup.com", "admin123");

        mockMvc.perform(get("/api/register")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectProtectedEndpointWithInvalidJwt() throws Exception {
        mockMvc.perform(get("/api/register")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    private String loginAndExtractToken(String username, String password) throws Exception {
        String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String token = extractToken(result.getResponse().getContentAsString());
        assertThat(token).isNotBlank();
        return token;
    }

    private String extractToken(String responseBody) {
        Matcher matcher = Pattern.compile("\\\"token\\\":\\\"([^\\\"]+)\\\"").matcher(responseBody);
        if (!matcher.find()) {
            throw new IllegalStateException("JWT token not found in response body: " + responseBody);
        }
        return matcher.group(1);
    }
}
