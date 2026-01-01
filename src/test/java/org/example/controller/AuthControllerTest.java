package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Login;
import org.example.repository.LoginRepository;
import org.example.security.JwtUtils;
import org.example.service.JpaUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static java.util.List.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authManager;

    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private JpaUserDetailsService userDetailsService;
    @MockBean
    private LoginRepository repo;

    @MockBean
    private PasswordEncoder encoder;


    @Test
    void login_Success_ReturnsToken() throws Exception {
        String username = "test";
        String password = "pass";
        String token = "jwt-token";

        when(authManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, null, of()));
        when(jwtUtils.generateToken(username)).thenReturn(token);

        var payload = objectMapper.writeValueAsString(new LoginRequest(username, password));

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    void register_Success_ReturnsOkMessage() throws Exception {
        String username = "newuser";
        String password = "pass";

        when(repo.findByUsername(username)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encoded-pass");
        when(repo.save(any(Login.class))).thenAnswer(inv -> inv.getArgument(0));

        var payload = objectMapper.writeValueAsString(new LoginRequest(username, password));

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().string("Registrado"));
    }

    @Test
    void register_UserExists_ReturnsBadRequest() throws Exception {
        String username = "exists";
        String password = "pass";

        when(repo.findByUsername(username)).thenReturn(Optional.of(Login.builder().username(username).build()));

        var payload = objectMapper.writeValueAsString(new LoginRequest(username, password));

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Usuario ya existe"));
    }

    // Helper record duplicated for test serialization
    record LoginRequest(String username, String password) {}
}
