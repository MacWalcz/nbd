package org.nbd.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.time.LocalDate;

public record RentDTO(
        @JsonSerialize(using = ToStringSerializer.class)
        ObjectId id,
    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    LocalDate startDate,
    LocalDate endDate,

    @NotNull(message = "Must not be null")
    ClientDTO client,
    @NotNull(message = "Must not be null")
    HouseDTO house,

    Double cost
) {}
