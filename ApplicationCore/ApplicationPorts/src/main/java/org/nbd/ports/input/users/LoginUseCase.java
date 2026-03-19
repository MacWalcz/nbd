package org.nbd.ports.input.users;

import org.nbd.dto.TokenResponse;
import org.nbd.dto.LoginRequest;

public interface LoginUseCase {
    TokenResponse login(LoginRequest request);
}
