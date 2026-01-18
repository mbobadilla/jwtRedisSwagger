package org.example.controller;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.security.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5500")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 1. Buscar al usuario por email
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 2. Comparar la contraseña enviada con el hash de la DB
            if (passwordEncoder.matches(request.getPasswordhash(), user.getPasswordHash())) {

                // 3. Si coincide, generar el token
                String token = jwtUtils.generateToken(user.getUsername());

                return ResponseEntity.ok(new AuthResponse(token));
            }
        }

        // 4. Si no existe o la clave es incorrecta, error 401
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciales incorrectas");
    }


}
