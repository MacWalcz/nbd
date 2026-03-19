package org.nbd.ports.output.auth;

import org.nbd.model.User;

public interface TokenPort {
    String generateToken(User user);
    String generateRefreshToken(User user);
    boolean validateToken(String token);
    String extractLogin(String refreshToken);
    String extractRole(String token);
}
