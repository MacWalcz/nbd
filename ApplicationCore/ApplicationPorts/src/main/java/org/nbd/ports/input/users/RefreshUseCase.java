package org.nbd.ports.input.users;

import org.nbd.dto.TokenResponse;

public interface RefreshUseCase {
    TokenResponse refresh(String refreshToken);
}
