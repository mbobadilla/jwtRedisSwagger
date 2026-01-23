package org.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
@Component
public class JwtUtils {
    private final Key key;
    private final long expirationMs=0;
   /* public JwtUtils(@Value("${jwt.secret}") String base64Secret,
                    @Value("${jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(Base64.getUrlDecoder().decode(base64Secret));
        this.expirationMs = expirationMs;

    }*/
// Constructor modificado para forzar una clave segura
   public JwtUtils(@org.springframework.beans.factory.annotation.Value("${jwt.secret:default}") String secret) {
       // 1. Usamos esta clave MAESTRA que cumple con los 256 bits y es válida en Base64
       String claveSegura = "VGhpcyBJcyBBIFZlcnkgTG9uZyBTZWNyZXQgS2V5IEZvciBKV1QgVG8gU2F0aXNmeSAyNTYgQml0cw==";

       // 2. Usamos getUrlDecoder que soporta todo tipo de caracteres
       byte[] keyBytes = java.util.Base64.getUrlDecoder().decode(claveSegura);

       // 3. Generamos la llave final
       this.key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
   }
    public String generateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationMs))
                .signWith(key)
                .compact();
    }
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
