package org.nbd.model;

import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "client_types")
public abstract class ClientType extends AbstractEntity {
    public abstract double getDiscount();

}
