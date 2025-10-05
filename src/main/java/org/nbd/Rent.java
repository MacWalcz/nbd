package org.nbd;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Rent {
    private LocalDate startDate;
    private LocalDate endDate;
    private Client client;
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

    public Rent(LocalDate startDate, LocalDate endDate, Client client, House house) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.client = client;
        this.house = house;
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        this.cost = daysBetween * house.getPrice() * client.getClientType().getDiscount();
    }
}
