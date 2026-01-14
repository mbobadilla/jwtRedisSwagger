package org.example.controller;

import org.example.model.User;
import org.example.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired // Ahora inyectado como Bean
    private PasswordEncoder passwordEncoder;

    // 🔹 Registrar nuevo usuario
    @PostMapping("/register")
    // CAMBIO: @RequestBody en lugar de @RequestParam
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {

        // Validación de existencia
        if (userRepository.existsByEmail(request.getEmail())) { // Asumiendo que username es el email
            return ResponseEntity.badRequest().body("El email ya está registrado.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        // Encriptar la contraseña que viene del request
        user.setPasswordHash(passwordEncoder.encode(request.getPasswordhash()));

        if (request.getRole() == null) {
            user.setRole(Role.consumer);
        } else {
            user.setRole(request.getRole());
        }


         userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    // 🔹 Iniciar sesión
    @PostMapping("/login")
    // CAMBIO: Usar LoginRequest en lugar de la entidad User directamente
    public ResponseEntity<?> login(@RequestBody LoginRequest credentials) {
        Optional<User> userOpt = userRepository.findByEmail(credentials.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }

        User user = userOpt.get();

        // Comprobar la contraseña del request contra el hash de la DB
        if (!passwordEncoder.matches(credentials.getPasswordhash(), user.getPasswordHash())) {
            return ResponseEntity.badRequest().body("Contraseña incorrecta.");
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/role/{role}")
    public List<User> getUsersByRole(@PathVariable Role role) {
        return userRepository.findByRole(role);
    }
}