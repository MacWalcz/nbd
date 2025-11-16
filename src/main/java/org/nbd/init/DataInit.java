package org.nbd.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
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
            def.setId("1");
            clientTypeRepo.save(def);
            ClientType pre = new Premium();
            pre.setId("2");
            clientTypeRepo.save(pre);
            ClientType lux = new Luxury();
            lux.setId("3");
            clientTypeRepo.save(lux);
        }

        if(clientRepo.findById("111111111111111111111111").isEmpty()) {
            Client client1 = Client.builder()
                    .id("111111111111111111111111")
                    .login("klient1")
                    .firstName("Jan")
                    .lastName("Kowalski")
                    .phoneNumber("789789789")
                    .active(true)
                    .clientType(clientTypeRepo.findById("1").orElse(null))
                    .build();
            clientRepo.save(client1);
        }

        if(clientRepo.findById("222222222222222222222222").isEmpty()) {
            Client client2 = Client.builder()
                    .id("222222222222222222222222")
                    .login("klient2")
                    .firstName("Anna")
                    .lastName("Nowak")
                    .phoneNumber("123123456")
                    .clientType(clientTypeRepo.findById("2").orElse(null))
                    .active(false) // неактивный клиент
                    .build();
            clientRepo.save(client2);
        }

        if(clientRepo.findById("333333333333333333333333").isEmpty()) {
            Client client3 = Client.builder()
                    .id("333333333333333333333333")
                    .login("klient1_copy") // похожий логин
                    .firstName("Piotr")
                    .lastName("Nowak")
                    .phoneNumber("555555555")
                    .active(true)
                    .clientType(clientTypeRepo.findById("3").orElse(null))
                    .build();
            clientRepo.save(client3);
        }

        if(houseRepo.findById("333333333333333333333333").isEmpty()) {
            House house1 = House.builder()
                    .id("333333333333333333333333")
                    .houseNumber("A89")
                    .price(100)
                    .area(55)
                    .build();
            houseRepo.save(house1);
        }

        if(houseRepo.findById("444444444444444444444444").isEmpty()) {
            House house2 = House.builder()
                    .id("444444444444444444444444")
                    .houseNumber("B56-8")
                    .price(355)
                    .area(200)
                    .build();
            houseRepo.save(house2);
        }

        if(houseRepo.findById("555555555555555555555555").isEmpty()) {
            House house3 = House.builder()
                    .id("555555555555555555555555")
                    .houseNumber("C12")
                    .price(250)
                    .area(120)
                    .build();
            houseRepo.save(house3);
        }

        // miniona
        if(rentRepo.findById("666666666666666666666666").isEmpty()) {
            Rent pastRent = Rent.builder()
                    .id("666666666666666666666666")
                    .client(clientRepo.findById("111111111111111111111111").orElse(null))
                    .house(houseRepo.findById("333333333333333333333333").orElse(null))
                    .startDate(LocalDate.of(2025, 10, 1))
                    .build();
            pastRent.endRent(LocalDate.of(2025, 10, 5));
            rentRepo.save(pastRent);
        }

        // bieżąca
        if(rentRepo.findById("777777777777777777777777").isEmpty()) {
            Rent currentRent = Rent.builder()
                    .id("777777777777777777777777")
                    .client(clientRepo.findById("333333333333333333333333").orElse(null))
                    .house(houseRepo.findById("444444444444444444444444").orElse(null))
                    .startDate(LocalDate.of(2025, 11, 10))
                    .build();
            rentRepo.save(currentRent);
        }

        // przyszła
        if(rentRepo.findById("888888888888888888888888").isEmpty()) {
            Rent futureRent = Rent.builder()
                    .id("888888888888888888888888")
                    .client(clientRepo.findById("222222222222222222222222").orElse(null))
                    .house(houseRepo.findById("555555555555555555555555").orElse(null))
                    .startDate(LocalDate.of(2025, 12, 1))
                    .build();
            rentRepo.save(futureRent);
        }
    }
}
