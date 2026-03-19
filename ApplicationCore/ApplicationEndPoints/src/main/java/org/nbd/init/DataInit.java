package org.nbd.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.adapters.repositories.*;
import org.nbd.model.*;
import org.nbd.ports.output.clients.ClientQueryPort;
import org.nbd.ports.output.clients.types.ClientTypeCommandPort;
import org.nbd.ports.output.clients.types.ClientTypeQueryPort;
import org.nbd.ports.output.houses.HouseCommandPort;
import org.nbd.ports.output.houses.HouseQueryPort;
import org.nbd.ports.output.rents.RentCommandPort;
import org.nbd.ports.output.rents.RentQueryPort;
import org.nbd.ports.output.users.UserCommandPort;
import org.nbd.ports.output.users.UserQueryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final UserCommandPort userCommandPort;
    private final UserQueryPort userQueryPort;
    private final HouseCommandPort houseCommandPort;
    private final HouseQueryPort houseQueryPort;
    private final RentCommandPort rentCommandPort;
    private final RentQueryPort rentQueryPort;
    private final ClientTypeQueryPort clientTypeQueryPort;
    private final ClientTypeCommandPort clientTypeCommandPort;
    private final PasswordEncoder passwordEncoder;
    private final ClientQueryPort clientQueryPort;


    @PostConstruct
    public void init() {

        String encodedPassword = passwordEncoder.encode("client123");

        if(clientTypeQueryPort.findById(new ObjectId("000000000000000000000001")).isEmpty()) {
            ClientType def = new Default();
            def.setId(new ObjectId("000000000000000000000001"));
            clientTypeCommandPort.save(def);
        }

        if (clientTypeQueryPort.findById(new ObjectId("0000000000000000000000002")).isEmpty()) {
            ClientType pre = new Premium();
            pre.setId(new ObjectId("000000000000000000000002"));
            clientTypeCommandPort.save(pre);
        }

        if(clientTypeQueryPort.findById(new ObjectId("0000000000000000000000003")).isEmpty()) {
            ClientType lux = new Luxury();
            lux.setId(new ObjectId("000000000000000000000003"));
            clientTypeCommandPort.save(lux);
        }


        System.out.println("Three Client Types Created!");


        if (userQueryPort.findById(new ObjectId("111111111111111111111111")).isEmpty()) {
            Client c1 = Client.builder()
                    .id(new ObjectId("111111111111111111111111"))
                    .login("klient1")
                    .password(encodedPassword)
                    .firstName("Jan")
                    .lastName("Kowalski")
                    .phoneNumber("789789789")
                    .active(true)
                    .clientType(clientTypeQueryPort.findById(new ObjectId("000000000000000000000001")).orElse(null))
                    .build();

            userCommandPort.save(c1);
            System.out.println("Client 1 Created!");
        }

        if (userQueryPort.findById(new ObjectId("222222222222222222222222")).isEmpty()) {
            Client c2 = Client.builder()
                    .id(new ObjectId("222222222222222222222222"))
                    .login("klient2")
                    .password(encodedPassword)
                    .firstName("Anna")
                    .lastName("Nowak")
                    .phoneNumber("123123456")
                    .active(false)
                    .clientType(clientTypeQueryPort.findById(new ObjectId("000000000000000000000002")).orElse(null))
                    .build();

            userCommandPort.save(c2);
            System.out.println("Client 2 Created!");
        }

        if (userQueryPort.findById(new ObjectId("333333333333333333333333")).isEmpty()) {
            Client c3 = Client.builder()
                    .id(new ObjectId("333333333333333333333333"))
                    .login("klient1_copy")
                    .password(encodedPassword)
                    .firstName("Piotr")
                    .lastName("Nowak")
                    .phoneNumber("555555555")
                    .active(true)
                    .clientType(clientTypeQueryPort.findById(new ObjectId("000000000000000000000003")).orElse(null))
                    .build();

            userCommandPort.save(c3);
            System.out.println("Client 3 Created!");
        }

        if (userQueryPort.findById(new ObjectId("333333333333333333333334")).isEmpty()) {
            Employee c3 = Employee.builder()
                    .id(new ObjectId("333333333333333333333334"))
                    .login("emplY")
                    .password(encodedPassword)
                    .firstName("Piotr")
                    .lastName("Nowak")
                    .phoneNumber("555555555")
                    .active(true)
                    .build();

            userCommandPort.save(c3);
            System.out.println("Employee Created!");
        }

        if (userQueryPort.findById(new ObjectId("333333333333333333333335")).isEmpty()) {
            Administrator c3 = Administrator.builder()
                    .id(new ObjectId("333333333333333333333335"))
                    .login("adMin12")
                    .password(encodedPassword)
                    .firstName("Piotr")
                    .lastName("Nowak")
                    .phoneNumber("555555555")
                    .active(true)
                    .build();

            userCommandPort.save(c3);
            System.out.println("Admin Created!");
        }

        if (houseQueryPort.findById(new ObjectId("444444444444444444444444")).isEmpty()) {
            House h1 = House.builder()
                    .id(new ObjectId("444444444444444444444444"))
                    .houseNumber("A89")
                    .price(100)
                    .area(55)
                    .build();

            houseCommandPort.save(h1);
            System.out.println("House 1 Created!");
        }

        if (houseQueryPort.findById(new ObjectId("555555555555555555555555")).isEmpty()) {
            House h2 = House.builder()
                    .id(new ObjectId("555555555555555555555555"))
                    .houseNumber("B56-8")
                    .price(355)
                    .area(200)
                    .build();

            houseCommandPort.save(h2);
            System.out.println("House 2 Created!");
        }

        if (houseQueryPort.findById(new ObjectId("666666666666666666666666")).isEmpty()) {
            House h3 = House.builder()
                    .id(new ObjectId("666666666666666666666666"))
                    .houseNumber("C12")
                    .price(250)
                    .area(120)
                    .build();

            houseCommandPort.save(h3);
            System.out.println("House 3 Created!");
        }

        if (rentQueryPort.findById(new ObjectId("777777777777777777777777")).isEmpty()) {
            Rent pastRent = Rent.builder()
                    .id(new ObjectId("777777777777777777777777"))
                    .client(clientQueryPort.findById(new ObjectId("111111111111111111111111")).orElse(null))
                    .house(houseQueryPort.findById(new ObjectId("444444444444444444444444")).orElse(null))
                    .startDate(LocalDate.of(2025, 10, 1))
                    .build();

            pastRent.endRent(LocalDate.of(2025, 10, 5));
            rentCommandPort.save(pastRent);
            System.out.println("Rent 1 Created!");
        }

        if (rentQueryPort.findById(new ObjectId("888888888888888888888888")).isEmpty()) {
            Rent currentRent = Rent.builder()
                    .id(new ObjectId("888888888888888888888888"))
                    .client(clientQueryPort.findById(new ObjectId("333333333333333333333333")).orElse(null))
                    .house(houseQueryPort.findById(new ObjectId("555555555555555555555555")).orElse(null))
                    .startDate(LocalDate.of(2025, 11, 10))
                    .build();

            rentCommandPort.save(currentRent);
            System.out.println("Rent 2 Created!");
        }

        if (rentQueryPort.findById(new ObjectId("999999999999999999999999")).isEmpty()) {
            Rent futureRent = Rent.builder()
                    .id(new ObjectId("999999999999999999999999"))
                    .client(clientQueryPort.findById(new ObjectId("222222222222222222222222")).orElse(null))
                    .house(houseQueryPort.findById(new ObjectId("666666666666666666666666")).orElse(null))
                    .startDate(LocalDate.of(2025, 12, 1))
                    .build();

            rentCommandPort.save(futureRent);
            System.out.println("Rent 3 Created!");
        }
    }
}
