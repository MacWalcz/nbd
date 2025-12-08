package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class House extends AbstractEntity {
    private String houseNumber;
    private double price;
    private double area;
}
