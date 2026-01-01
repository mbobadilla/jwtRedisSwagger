package org.example.repository;

import org.example.model.Login;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;




    public interface LoginRepository extends JpaRepository<Login, Long> {
        Optional<Login> findByUsername(String username);
    }

