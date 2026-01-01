package org.example.controller;

import org.example.model.Login;
import org.example.controller.LoginRequest;
import org.example.repository.LoginRepository;
import org.example.repository.UserRepository;
import org.example.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final LoginRepository repo;
    private final PasswordEncoder encoder;

    public AuthController(AuthenticationManager am, JwtUtils jwtUtils, LoginRepository repo, PasswordEncoder encoder) {
        this.authManager = am;
        this.jwtUtils = jwtUtils;
        this.repo = repo;
        this.encoder = encoder;
    }

    record LoginRequest(String username, String password) {}
    record AuthResponse(String token) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        String token = jwtUtils.generateToken(req.username());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest req) {
        if (repo.findByUsername(req.username()).isPresent()) {

            return ResponseEntity.badRequest().body("Usuario ya existe");
        }
        Login u = Login.builder()
                .username(req.username())
                .password(encoder.encode(req.password()))
                .idStatus(1)

                .build();
        repo.save(u);
        return ResponseEntity.ok("Registrado");
    }
}
