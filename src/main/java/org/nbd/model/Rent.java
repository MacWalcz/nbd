package org.nbd.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@SuperBuilder
public class Rent extends AbstractEntity {
    private LocalDate startDate;
    private LocalDate endDate;
    private Client client;
    private House house;
    private double cost;


    public Rent(LocalDate startDate, LocalDate endDate, Client client, House house) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.client = client;
        this.house = house;
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        this.cost = daysBetween * house.getPrice() * client.getClientType().getDiscount();
    }
}
