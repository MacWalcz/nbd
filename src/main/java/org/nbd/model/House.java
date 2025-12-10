package org.nbd.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import com.datastax.oss.driver.api.mapper.entity.naming.GetterStyle;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity(defaultKeyspace = "rent_a_house")
@CqlName("HousesIds")
@PropertyStrategy(getterStyle = GetterStyle.JAVABEANS)
public class House extends AbstractEntity {
    private String houseNumber;

    private double price;

    private double area;
}