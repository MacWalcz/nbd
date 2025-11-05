package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@SuperBuilder
@Document(collection = "houses")
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
