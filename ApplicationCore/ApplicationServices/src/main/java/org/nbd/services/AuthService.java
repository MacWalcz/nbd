package org.nbd.services;

import lombok.RequiredArgsConstructor;
import org.nbd.dto.JwtResponse;
import org.nbd.dto.LoginRequest;
import org.nbd.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public JwtResponse login(LoginRequest request) {
        User user = userService.getByLogin(request.login());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String access = jwtService.generateToken(user);
        String refresh = jwtService.generateRefreshToken(user);

        return new JwtResponse(access, refresh);
    }

    public JwtResponse refresh(String refreshToken) {
        if (!jwtService.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid Refresh Token");
        }

        String login = jwtService.extractLogin(refreshToken);
        User user = userService.getByLogin(login);

        String newAccess = jwtService.generateToken(user);
        return new JwtResponse(newAccess, refreshToken);
    }
}
