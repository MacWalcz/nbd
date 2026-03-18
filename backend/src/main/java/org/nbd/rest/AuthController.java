package org.nbd.rest;

import lombok.RequiredArgsConstructor;
import org.nbd.dto.JwtResponse;
import org.nbd.dto.LoginRequest;
import org.nbd.dto.RefreshDTO;
import org.nbd.model.User;
import org.nbd.services.AuthService;
import org.nbd.services.JwtService;
import org.nbd.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody RefreshDTO refreshToken) {
        return authService.refresh(refreshToken.refreshToken());
    }
}