package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

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

    private Double cost = 0.0;


    public void endRent(LocalDate endDate) {
        this.endDate = endDate;
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        this.cost = daysBetween * house.getPrice();
    }

    public boolean isActive() {
        return endDate == null;
    }
}
