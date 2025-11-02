package org.nbd;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nbd.model.Client;
import org.nbd.model.Default;
import org.nbd.model.House;
import org.nbd.model.Rent;

import java.time.LocalDate;
import java.util.UUID;

public class RentTest {
    private Rent rent;
    private Client client;
    private House house;
    @BeforeEach
    void setUp() {
        house = new House("1",200.0,30.0);
        client = Client.builder()
                .id(UUID.randomUUID())
                .login("youdeen")
                .firstName("Kamil")
                .lastName("Kowal")
                .phoneNumeber("504420021")
                .clientType(new Default())
                .build();

        rent = new Rent(LocalDate.of(2025,10,10), LocalDate.of(2025,10,13), client, house);
    }
    @Test
    void testRent() {
        assertTrue(rent.getClient().equals(client));
        assertTrue(rent.getHouse().equals(house));
        double cost = rent.getCost();
        assertTrue(cost == 600.0);
    }
}
