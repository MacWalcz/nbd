package org.nbd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.nbd.model.Client;
import org.nbd.model.ClientType;

public record ClientDTO(
    String id,
    @NotBlank(message = "Login cannot be blank")
    @Size(min = 3, max = 30, message = "Login must be between 3 and 30 characters")
    String login,
    @NotBlank(message = "First name cannot be blank")
    String firstName,
    @NotBlank(message = "Last name cannot be blank")
    String lastName,
    @Size(min = 7, max = 15, message = "Phone number must be between 7 and 15 characters")
    String phoneNumber,
    boolean active,
    @NotBlank(message = "Client Type cannot be blank")
    String clientType
) {}
