package org.example.service;

import org.example.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    /**
     * Autentica las credenciales y devuelve el token JWT.
     * @param username nombre de usuario
     * @param password contraseña
     * @return token JWT si la autenticación es exitosa
     * @throws AuthenticationException si la autenticación falla
     */
    public String loginAndGetToken(String username, String password) throws AuthenticationException {
        Authentication authRequest = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authResult = authenticationManager.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        return jwtUtils.generateToken(username);
    }
}
