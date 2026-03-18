package org.nbd.services;

import io.jsonwebtoken.*;
import org.nbd.model.Administrator;
import org.nbd.model.Employee;
import org.nbd.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getLogin())
                .claim("id", user.getId().toString())
                .claim("role", getRoleFromUser(user))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String extractLogin(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRole(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        String role = extractRole(token);
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 5))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getRoleFromUser(User user) {
        if (user instanceof Administrator) return "ADMINISTRATOR";
        if (user instanceof Employee) return "EMPLOYEE";
        return "CLIENT";
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            // Log: Niepoprawny podpis JWT
        } catch (ExpiredJwtException e) {
            // Log: Token wygasł
        } catch (Exception e) {
            // Log: Inny błąd walidacji
        }
        return false;
    }
}