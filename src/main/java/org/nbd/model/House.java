package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "houses")
public class House extends AbstractEntity {
    private String houseNumber;
    private double price;
    private double area;
}
