package org.nbd.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "Must not be blank") @NotNull @Size(min = 2, max = 20)
    String login,
    @NotBlank(message = "Must not be blank") @NotNull @Size(min = 2, max = 20)
    String password
) {}
