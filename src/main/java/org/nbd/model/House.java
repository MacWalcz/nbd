package org.nbd.model;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import com.datastax.oss.driver.api.mapper.entity.naming.GetterStyle;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;


@Entity(defaultKeyspace = "rent_a_house")
@CqlName("houses")
@PropertyStrategy(getterStyle = GetterStyle.JAVABEANS)
public class House extends AbstractEntity {


    @CqlName("house_number")
    private String houseNumber;

    @ClusteringColumn
    private double price;

    private double area;

    public House(UUID id, String houseNumber, double price, double area) {
        super(id);
        this.houseNumber = houseNumber;
        this.price = price;
        this.area = area;
    }

    public String getHouseNumber() {
        return houseNumber;
    }


    @Override
    public UUID getId() {
        return id;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public House() {
        super();
    }
}