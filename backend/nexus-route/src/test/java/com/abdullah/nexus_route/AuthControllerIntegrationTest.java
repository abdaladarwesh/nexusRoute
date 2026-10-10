package com.abdullah.nexus_route;

import com.abdullah.nexus_route.dto.response.AuthResponse;
import com.abdullah.nexus_route.dto.request.LoginRequest;
import com.abdullah.nexus_route.dto.request.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Demo user should be seeded and login successfully")
    void demoUser_shouldLoginSuccessfully() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("demo@nexusroute.com")
                .password("Password123!")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.user.email").value("demo@nexusroute.com"))
                .andExpect(jsonPath("$.user.fullName").value("Demo User"))
                .andExpect(jsonPath("$.user.role").value("ROLE_USER"));
    }

    @Test
    @DisplayName("Sign up should create new user and return JWT token")
    void signUp_shouldCreateNewUserAndReturnToken() throws Exception {
        String uniqueEmail = "user_" + System.currentTimeMillis() + "@example.com";
        RegisterRequest request = RegisterRequest.builder()
                .email(uniqueEmail)
                .password("StrongPassword123!")
                .fullName("Nexus Explorer")
                .build();

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.user.email").value(uniqueEmail))
                .andExpect(jsonPath("$.user.fullName").value("Nexus Explorer"));
    }

    @Test
    @DisplayName("Sign up with existing email should return 409 Conflict")
    void signUp_duplicateEmail_shouldReturnConflict() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .email("demo@nexusroute.com")
                .password("AnotherPassword123!")
                .fullName("Demo Impostor")
                .build();

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email 'demo@nexusroute.com' is already registered"));
    }

    @Test
    @DisplayName("Login with wrong password should return 401 Unauthorized")
    void login_invalidCredentials_shouldReturnUnauthorized() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("demo@nexusroute.com")
                .password("WrongPassword999!")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    @DisplayName("Accessing protected path without token should return 401 Unauthorized")
    void protectedPath_withoutToken_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/path")
                        .param("startId", "1")
                        .param("destinationId", "2"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Current user profile should be retrievable with valid JWT")
    void getCurrentUser_withValidToken_shouldReturnProfile() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("demo@nexusroute.com")
                .password("Password123!")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        AuthResponse authResponse = objectMapper.readValue(
                loginResult.getResponse().getContentAsString(),
                AuthResponse.class
        );

        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + authResponse.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("demo@nexusroute.com"))
                .andExpect(jsonPath("$.fullName").value("Demo User"));
    }
}
