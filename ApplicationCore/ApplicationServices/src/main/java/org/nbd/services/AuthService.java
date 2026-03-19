package org.nbd.services;

import lombok.RequiredArgsConstructor;
import org.nbd.dto.TokenResponse;
import org.nbd.dto.LoginRequest;
import org.nbd.model.User;
import org.nbd.ports.input.users.LoginUseCase;
import org.nbd.ports.input.users.RefreshUseCase;
import org.nbd.ports.output.auth.TokenPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements LoginUseCase, RefreshUseCase {

    private final UserService userService;
    private final TokenPort tokenPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse login(LoginRequest request) {
        User user = userService.getByLogin(request.login());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String access = tokenPort.generateToken(user);
        String refresh = tokenPort.generateRefreshToken(user);

        return new TokenResponse(access, refresh);
    }

    @Override
    public TokenResponse refresh(String refreshToken) {
        if (!tokenPort.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid Refresh Token");
        }

        String login = tokenPort.extractLogin(refreshToken);
        User user = userService.getByLogin(login);

        String newAccess = tokenPort.generateToken(user);
        return new TokenResponse(newAccess, refreshToken);
    }
}
