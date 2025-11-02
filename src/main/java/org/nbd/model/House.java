package org.nbd.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class House extends AbstractEntity {
    private String houseNumber;
    private double price;
    private double area;

    public House(String houseNumber, double price, double area) {
        this.houseNumber = houseNumber;
        this.price = price;
        this.area = area;
    }
}


