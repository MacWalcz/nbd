package org.nbd.adapters.auth;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.nbd.adapters.auth.mappers.AuthUserMapper;
import org.nbd.adapters.auth.model.AuthUser;
import org.nbd.model.Administrator;
import org.nbd.model.Employee;
import org.nbd.model.User;
import org.nbd.ports.output.auth.TokenPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAdapter implements TokenPort {


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Override
    public String generateToken(User user) {
        AuthUser authUser = AuthUserMapper.toAuthUser(user);
        return Jwts.builder()
                .setSubject(authUser.login())
                .claim("id", authUser.id().toString())
                .claim("role", authUser.role())
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

    @Override
    public String generateRefreshToken(User user) {
        AuthUser authUser = AuthUserMapper.toAuthUser(user);
        return Jwts.builder()
                .setSubject(authUser.login())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 5))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {

        } catch (ExpiredJwtException e) {

        } catch (Exception e) {

        }
        return false;
    }
}