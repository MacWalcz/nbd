package org.nbd.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.*;
import org.bson.types.ObjectId;

public class HouseDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @NotBlank(message = "Must not be blank")
    private String houseNumber;

    @NotNull(message = "Must not be blank")
    @Min(value = 1, message = "Can't be below 1")
    private Double price;

    @NotNull(message = "Must not be blank")
    @Min(value = 1, message = "Can't be below 1")
    private Double area;

    public HouseDTO() {}

    public HouseDTO(ObjectId id, String houseNumber, Double price, Double area) {
        this.id = id;
        this.houseNumber = houseNumber;
        this.price = price;
        this.area = area;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getArea() { return area; }
    public void setArea(Double area) { this.area = area; }
}
