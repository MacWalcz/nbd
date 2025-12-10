package org.nbd.model;


import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "rent_a_house")
@CqlName("ClientTypesIds")
public class Default extends ClientType  {
    @Override
    public double getDiscount() {
        return 1;
    }
}
