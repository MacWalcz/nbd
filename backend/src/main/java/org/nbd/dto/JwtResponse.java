package org.nbd.dto;

public record JwtResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
    // Добавим конструктор по умолчанию для удобства
    public JwtResponse(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, "Bearer");
    }
}