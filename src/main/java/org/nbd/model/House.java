package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
//@Document(collection = "houses")
public class House extends AbstractEntity {
    private String houseNumber;
    private double price;
    private double area;
}
