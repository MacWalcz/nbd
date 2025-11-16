package org.nbd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.nbd.model.Client;
import org.nbd.model.House;

import java.time.LocalDate;

public record RentDTO(
    String id,
    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    LocalDate startDate,
    LocalDate endDate,

    @NotNull(message = "Must not be null")
    Client client,
    @NotNull(message = "Must not be null")
    House house,

    Double cost
) {}
