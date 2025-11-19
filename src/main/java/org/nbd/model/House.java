package org.nbd.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class House extends AbstractEntity {
    private String houseNumber;
    private double price;
    private double area;
}
