package org.nbd.init;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.nbd.model.*;
import org.nbd.repositories.HouseRepo;
import org.nbd.repositories.RentRepo;
import org.nbd.repositories.UserRepo;

import java.time.LocalDate;

@ApplicationScoped
public class DataInit {

    @Inject
    private UserRepo userRepo;

    @Inject
    private HouseRepo houseRepo;

    @Inject
    private RentRepo rentRepo;

    @PostConstruct
    public void init() {
        System.out.println("Initializing data...");

        Client c1 = Client.builder()
                .id(new ObjectId("111111111111111111111111"))
                .login("klient1")
                .firstName("Jan")
                .lastName("Kowalski")
                .phoneNumber("789789789")
                .active(true)
                .build();
        insertUserIfNotExists(c1, "Client 1");

        Client c2 = Client.builder()
                .id(new ObjectId("222222222222222222222222"))
                .login("klient2")
                .firstName("Anna")
                .lastName("Nowak")
                .phoneNumber("123123456")
                .active(false)
                .build();
        insertUserIfNotExists(c2, "Client 2");

        Client c3 = Client.builder()
                .id(new ObjectId("333333333333333333333333"))
                .login("klient1_copy")
                .firstName("Piotr")
                .lastName("Nowak")
                .phoneNumber("555555555")
                .active(true)
                .build();
        insertUserIfNotExists(c3, "Client 3");

        House h1 = House.builder()
                .id(new ObjectId("444444444444444444444444"))
                .houseNumber("A89")
                .price(100)
                .area(55)
                .build();
        insertHouseIfNotExists(h1, "House 1");

        House h2 = House.builder()
                .id(new ObjectId("555555555555555555555555"))
                .houseNumber("B56-8")
                .price(355)
                .area(200)
                .build();
        insertHouseIfNotExists(h2, "House 2");

        House h3 = House.builder()
                .id(new ObjectId("666666666666666666666666"))
                .houseNumber("C12")
                .price(250)
                .area(120)
                .build();
        insertHouseIfNotExists(h3, "House 3");

        Client foundC1 = (Client) userRepo.findClientById(new ObjectId("111111111111111111111111"));
        Client foundC3 = (Client) userRepo.findClientById(new ObjectId("333333333333333333333333"));
        Client foundC2 = (Client) userRepo.findClientById(new ObjectId("222222222222222222222222"));


        Rent pastRent = Rent.builder()
                .id(new ObjectId("777777777777777777777777"))
                .client(foundC1)
                .house(h1)
                .startDate(LocalDate.of(2025, 10, 1))
                .build();
        if (pastRent.getEndDate() == null) {
            pastRent.endRent(LocalDate.of(2025, 10, 5));
        }
        insertRentIfNotExists(pastRent, "Rent 1");

        Rent currentRent = Rent.builder()
                .id(new ObjectId("888888888888888888888888"))
                .client(foundC3)
                .house(h2)
                .startDate(LocalDate.of(2025, 11, 10))
                .build();
        insertRentIfNotExists(currentRent, "Rent 2");

        Rent futureRent = Rent.builder()
                .id(new ObjectId("999999999999999999999999"))
                .client(foundC2)
                .house(h3)
                .startDate(LocalDate.of(2025, 12, 1))
                .build();
        insertRentIfNotExists(futureRent, "Rent 3");
    }


    private void insertUserIfNotExists(Client c, String name) {
        if (userRepo.findClientById(c.getId()) == null) {
            userRepo.saveClient(c);
            System.out.println(name + " Created!");
        }
    }

    private void insertHouseIfNotExists(House h, String name) {
        if (houseRepo.findById(h.getId()) == null) {
            houseRepo.save(h);
            System.out.println(name + " Created!");
        }
    }

    private void insertRentIfNotExists(Rent r, String name) {
        if (rentRepo.findById(r.getId()) == null) {

            try {
                rentRepo.save(r);
                System.out.println(name + " Created!");
            } catch (Exception e) {
                System.err.println(name + " not created due to overlap/exception: " + e.getMessage());
            }
        }
    }
}