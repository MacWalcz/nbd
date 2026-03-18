package org.nbd.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;

public record HouseDTO (
        @JsonSerialize(using = ToStringSerializer.class)
        ObjectId id,
    @NotBlank(message = "Must not be blank")
    String houseNumber,
    @NotNull(message = "Must not be blank")
    @Min(value = 1, message = "Can't be below 1")
    double price,
    @NotNull(message = "Must not be blank")
    @Min(value = 1, message = "Can't be below 1")
    double area
) {}
