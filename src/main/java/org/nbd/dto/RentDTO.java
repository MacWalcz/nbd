package org.nbd.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.*;
import org.bson.types.ObjectId;

import java.time.LocalDate;

public class RentDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @NotNull(message = "Must not be null")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Must not be null")
    private ClientDTO client;

    @NotNull(message = "Must not be null")
    private HouseDTO house;

    private Double cost;

    public RentDTO() {}

    public RentDTO(ObjectId id, LocalDate startDate, LocalDate endDate,
                   ClientDTO client, HouseDTO house, Double cost) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.client = client;
        this.house = house;
        this.cost = cost;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public ClientDTO getClient() { return client; }
    public void setClient(ClientDTO client) { this.client = client; }

    public HouseDTO getHouse() { return house; }
    public void setHouse(HouseDTO house) { this.house = house; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }
}
