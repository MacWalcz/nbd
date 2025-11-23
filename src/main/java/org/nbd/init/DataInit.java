package org.nbd.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.model.*;
import org.nbd.repositories.ClientRepo;
import org.nbd.repositories.ClientTypeRepo;
import org.nbd.repositories.HouseRepo;
import org.nbd.repositories.RentRepo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final ClientRepo clientRepo;
    private final HouseRepo houseRepo;
    private final RentRepo rentRepo;
    private final ClientTypeRepo clientTypeRepo;

    @PostConstruct
    public void init() {

        if (clientTypeRepo.count() == 0) {
            ClientType def = new Default();
            def.setId(new ObjectId("000000000000000000000001"));
            clientTypeRepo.save(def);

            ClientType pre = new Premium();
            pre.setId(new ObjectId("000000000000000000000002"));
            clientTypeRepo.save(pre);

            ClientType lux = new Luxury();
            lux.setId(new ObjectId("000000000000000000000003"));
            clientTypeRepo.save(lux);

            System.out.println("Three Client Types Created!");
        }

        if (clientRepo.findById(new ObjectId("111111111111111111111111")).isEmpty()) {
            Client c1 = Client.builder()
                    .id(new ObjectId("111111111111111111111111"))
                    .login("klient1")
                    .firstName("Jan")
                    .lastName("Kowalski")
                    .phoneNumber("789789789")
                    .active(true)
                    .clientType(clientTypeRepo.findById(new ObjectId("000000000000000000000001")).orElse(null))
                    .build();

            clientRepo.save(c1);
            System.out.println("Client 1 Created!");
        }

        if (clientRepo.findById(new ObjectId("222222222222222222222222")).isEmpty()) {
            Client c2 = Client.builder()
                    .id(new ObjectId("222222222222222222222222"))
                    .login("klient2")
                    .firstName("Anna")
                    .lastName("Nowak")
                    .phoneNumber("123123456")
                    .active(false)
                    .clientType(clientTypeRepo.findById(new ObjectId("000000000000000000000002")).orElse(null))
                    .build();

            clientRepo.save(c2);
            System.out.println("Client 2 Created!");
        }

        if (clientRepo.findById(new ObjectId("333333333333333333333333")).isEmpty()) {
            Client c3 = Client.builder()
                    .id(new ObjectId("333333333333333333333333"))
                    .login("klient1_copy")
                    .firstName("Piotr")
                    .lastName("Nowak")
                    .phoneNumber("555555555")
                    .active(true)
                    .clientType(clientTypeRepo.findById(new ObjectId("000000000000000000000003")).orElse(null))
                    .build();

            clientRepo.save(c3);
            System.out.println("Client 3 Created!");
        }

        if (houseRepo.findById(new ObjectId("444444444444444444444444")).isEmpty()) {
            House h1 = House.builder()
                    .id(new ObjectId("444444444444444444444444"))
                    .houseNumber("A89")
                    .price(100)
                    .area(55)
                    .build();

            houseRepo.save(h1);
            System.out.println("House 1 Created!");
        }

        if (houseRepo.findById(new ObjectId("555555555555555555555555")).isEmpty()) {
            House h2 = House.builder()
                    .id(new ObjectId("555555555555555555555555"))
                    .houseNumber("B56-8")
                    .price(355)
                    .area(200)
                    .build();

            houseRepo.save(h2);
            System.out.println("House 2 Created!");
        }

        if (houseRepo.findById(new ObjectId("666666666666666666666666")).isEmpty()) {
            House h3 = House.builder()
                    .id(new ObjectId("666666666666666666666666"))
                    .houseNumber("C12")
                    .price(250)
                    .area(120)
                    .build();

            houseRepo.save(h3);
            System.out.println("House 3 Created!");
        }

        if (rentRepo.findById(new ObjectId("777777777777777777777777")).isEmpty()) {
            Rent pastRent = Rent.builder()
                    .id(new ObjectId("777777777777777777777777"))
                    .client(clientRepo.findById(new ObjectId("111111111111111111111111")).orElse(null))
                    .house(houseRepo.findById(new ObjectId("444444444444444444444444")).orElse(null))
                    .startDate(LocalDate.of(2025, 10, 1))
                    .build();

            pastRent.endRent(LocalDate.of(2025, 10, 5));
            rentRepo.save(pastRent);
            System.out.println("Rent 1 Created!");
        }

        if (rentRepo.findById(new ObjectId("888888888888888888888888")).isEmpty()) {
            Rent currentRent = Rent.builder()
                    .id(new ObjectId("888888888888888888888888"))
                    .client(clientRepo.findById(new ObjectId("333333333333333333333333")).orElse(null))
                    .house(houseRepo.findById(new ObjectId("555555555555555555555555")).orElse(null))
                    .startDate(LocalDate.of(2025, 11, 10))
                    .build();

            rentRepo.save(currentRent);
            System.out.println("Rent 2 Created!");
        }

        if (rentRepo.findById(new ObjectId("999999999999999999999999")).isEmpty()) {
            Rent futureRent = Rent.builder()
                    .id(new ObjectId("999999999999999999999999"))
                    .client(clientRepo.findById(new ObjectId("222222222222222222222222")).orElse(null))
                    .house(houseRepo.findById(new ObjectId("666666666666666666666666")).orElse(null))
                    .startDate(LocalDate.of(2025, 12, 1))
                    .build();

            rentRepo.save(futureRent);
            System.out.println("Rent 3 Created!");
        }
    }
}
