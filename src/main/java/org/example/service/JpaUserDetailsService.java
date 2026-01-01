package org.example.service;

import org.example.model.Login;
import org.example.repository.LoginRepository;
import org.example.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final LoginRepository repo;

    public JpaUserDetailsService(LoginRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Login user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );
    }
}
