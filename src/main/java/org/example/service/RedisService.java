package org.example.service;

import io.swagger.v3.oas.annotations.servers.Server;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RedisService {
    @Autowired
    private UserRepository userRepository;

    // La próxima vez que se pida este ID, el dato saldrá de Redis, no de la DB
    @Cacheable(value = "users", key = "#id")
    public User getUserById(UUID id) {
        System.out.println("Consultando en PostgreSQL...");
        return userRepository.findById(id).orElse(null);
    }

    // Cuando el usuario se actualice, borramos la caché para evitar datos viejos
    @CacheEvict(value = "users", key = "#user.id")
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
