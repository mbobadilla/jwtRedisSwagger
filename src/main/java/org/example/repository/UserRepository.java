package org.example.repository;

import org.example.model.Role;
import org.example.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Buscar usuario por email
    Optional<User> findByEmail(String email);

    // Filtrar usuarios por rol (consumer, provider, admin)
    List<User> findByRole(Role role);

    // Verificar existencia de usuario por email
    boolean existsByEmail(String email);

   Optional<User> findById(UUID id);
}

