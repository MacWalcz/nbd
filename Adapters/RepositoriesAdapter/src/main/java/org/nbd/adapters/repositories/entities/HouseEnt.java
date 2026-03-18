package org.nbd.adapters.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "houses")
public class HouseEnt extends AbstractEnt {
    private String houseNumber;
    private double price;
    private double area;
}
