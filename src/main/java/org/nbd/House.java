package org.nbd;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "HOUSES")
@Access(AccessType.FIELD)
public class House extends AbstractEntity {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(initialValue = 1, name = "houseIdSequence")
    @GeneratedValue(generator = "houseIdSequence")
    private long id;

    private String houseNumber;
    private double price;
    private double area;


    public String getHouseNumber() {
        return houseNumber;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public House(){

    }

    public House(String houseNumber, double price, double area) {
        this.houseNumber = houseNumber;
        this.price = price;
        this.area = area;
    }
}


