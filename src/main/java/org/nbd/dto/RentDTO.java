package org.nbd.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.nbd.model.Client;
import org.nbd.model.House;

import java.time.LocalDate;

public record RentDTO(
        @JsonSerialize(using = ToStringSerializer.class)
        ObjectId id,
    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    LocalDate startDate,
    LocalDate endDate,

    @NotNull(message = "Must not be null")
    ClientDTO clientDTO,
    @NotNull(message = "Must not be null")
    HouseDTO houseDTO,

    Double cost
) {}
