package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@SuperBuilder
@Document(collection = "rents")
public class Rent extends AbstractEntity {
    private LocalDate startDate;
    private LocalDate endDate;

    @DBRef
    private Client client;

    @DBRef
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
