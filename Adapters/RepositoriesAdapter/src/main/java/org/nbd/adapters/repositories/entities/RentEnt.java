package org.nbd.adapters.repositories.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@SuperBuilder
@Document(collection = "rents")
public class RentEnt extends AbstractEnt {

    private LocalDate startDate;
    private LocalDate endDate;

    @DBRef
    private ClientEnt clientEnt;

    @DBRef
    private HouseEnt house;

    private Double cost;

    public RentEnt(LocalDate startDate, ClientEnt clientEnt, HouseEnt house) {
        this.startDate = startDate;
        this.clientEnt = clientEnt;
        this.house = house;
        this.cost = null;
    }

    public void endRent(LocalDate endDate) {
        this.endDate = endDate;
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        this.cost = daysBetween * house.getPrice() * clientEnt.getClientTypeEnt().getDiscount();
    }

    public boolean isActive() {
        return endDate == null;
    }
}

