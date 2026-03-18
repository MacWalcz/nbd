package org.nbd.ports.input.users;

import org.nbd.dto.JwtResponse;
import org.nbd.dto.LoginRequest;

public interface LoginUseCase {
    JwtResponse login(LoginRequest request);
}
