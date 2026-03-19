package org.nbd.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
    // jakiś szpont na front ?
    public TokenResponse(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, "Bearer");
    }
}