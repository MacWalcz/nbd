package org.nbd.adapters.repositories.entities;


import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "client_types")
public abstract class ClientTypeEnt extends AbstractEnt {
    public abstract double getDiscount();

}
