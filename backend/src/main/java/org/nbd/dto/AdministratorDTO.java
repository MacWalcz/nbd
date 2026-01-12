package org.nbd.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;

public record AdministratorDTO(

        @JsonSerialize(using = ToStringSerializer.class)
    ObjectId id,
    @NotBlank(message = "Login cannot be blank")
    @Size(min = 3, max = 30, message = "Login must be between 3 and 30 characters")
    String login,
    @NotBlank(message = "First name cannot be blank")
    String firstName,
    @NotBlank(message = "Last name cannot be blank")
    String lastName,
    @Size(min = 7, max = 15, message = "Login must be between 3 and 30 characters")
    String phoneNumber,
    @NotNull
    boolean active
) {}
