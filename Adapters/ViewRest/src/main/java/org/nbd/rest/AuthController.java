package org.nbd.rest;

import lombok.RequiredArgsConstructor;
import org.nbd.dto.TokenResponse;
import org.nbd.dto.LoginRequest;
import org.nbd.dto.RefreshDTO;
import org.nbd.services.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshDTO refreshToken) {
        return authService.refresh(refreshToken.refreshToken());
    }
}