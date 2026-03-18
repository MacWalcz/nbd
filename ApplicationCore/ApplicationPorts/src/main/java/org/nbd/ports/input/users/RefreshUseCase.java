package org.nbd.ports.input.users;

import org.nbd.dto.JwtResponse;

public interface RefreshUseCase {
    JwtResponse refresh(String refreshToken);
}
