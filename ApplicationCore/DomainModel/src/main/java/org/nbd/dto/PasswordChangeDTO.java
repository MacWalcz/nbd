package org.nbd.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordChangeDTO(
    @NotBlank @NotBlank
    String newPassword
) {}
