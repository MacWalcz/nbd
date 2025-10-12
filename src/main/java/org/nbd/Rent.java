package org.nbd;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Access(AccessType.FIELD)
@Entity
@Table(name = "RENTS")
public class Rent extends AbstractEntity {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(initialValue = 1, name = "rentIdSequence")
    @GeneratedValue(generator = "rentIdSequence")
    private long id;


    private LocalDate startDate;


    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID")
    @NotNull
    private Client client;

    @ManyToOne
    @JoinColumn(name = "HOUSE_ID")
    @NotNull
    private House house;

    private double cost;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Rent(){

    }

    public Rent(LocalDate startDate, LocalDate endDate, Client client, House house) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.client = client;
        this.house = house;
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        this.cost = daysBetween * house.getPrice() * client.getClientType().getDiscount();
    }
}
