package org.nbd;

import org.junit.jupiter.api.*;
import org.nbd.exceptions.HouseNotAvaibleException;
import org.nbd.model.*;
import org.nbd.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RepositoryClusterIntegrationTest {

    @Autowired
    private ClientRepo clientRepository;

    @Autowired
    private HouseRepo houseRepository;

    @Autowired
    private RentRepo rentRepository;

    @Autowired
    private ClientTypeRepo clientTypeRepository;

    private Client testClient;
    private House testHouse;
    private ClientType testClientType;

    @BeforeEach
    void setup() {
        clientRepository.deleteAll();
        houseRepository.deleteAll();
        rentRepository.deleteAll();
        clientTypeRepository.deleteAll();

        testClientType = new Default();

        testClient = Client.builder()
                .login("karabeika")
                .firstName("Mikita")
                .lastName("Test")
                .phoneNumeber("505050505")
                .clientType(testClientType)
                .active(true)
                .build();

        testHouse = House.builder()
                .houseNumber("d67")
                .area(30.0)
                .price(200.0)
                .build();

        clientTypeRepository.save(testClientType);
        clientRepository.save(testClient);
        houseRepository.save(testHouse);
    }

    @Test
    @Order(1)
    void testClientCRUD() {

        Client client = Client.builder()
                .login("user1")
                .firstName("Jan")
                .lastName("Kowalski")
                .phoneNumeber("123456789")
                .clientType(testClientType)
                .build();

        clientRepository.save(client);


        Optional<Client> found = clientRepository.findById(client.getId());
        assertThat(found).isPresent();


        found.get().setLastName("Nowak");
        clientRepository.save(found.get());
        Client updated = clientRepository.findById(client.getId()).orElseThrow();
        assertThat(updated.getLastName()).isEqualTo("Nowak");


        clientRepository.deleteById(client.getId());
        assertThat(clientRepository.findById(client.getId())).isEmpty();
    }

    @Test
    @Order(2)
    void testHouseCRUD() {
        House house = House.builder()
                .houseNumber("H2")
                .price(300.0)
                .area(80.0)
                .build();


        houseRepository.save(house);

        House found = houseRepository.findById(house.getId()).orElse(null);
        assertThat(found).isNotNull();

        found.setPrice(350.0);
        houseRepository.save(found);

        House updated = houseRepository.findById(house.getId()).orElse(null);
        assertThat(updated.getPrice()).isEqualTo(350.0);

        houseRepository.deleteById(house.getId());
        assertThat(houseRepository.findById(house.getId())).isEmpty();
    }

    @Test
    @Order(3)
    void testRentCRUD() {
        Rent rent = new Rent(LocalDate.now(), LocalDate.now().plusDays(3), testClient, testHouse);
        rentRepository.saveRent(rent);

        Rent found = rentRepository.findById(rent.getId()).orElse(null);
        assertThat(found).isNotNull();

        found.setEndDate(LocalDate.now().plusDays(5));
        rentRepository.saveRent(found);
        Rent updated = rentRepository.findById(rent.getId()).orElse(null);
        assertThat(updated.getEndDate()).isAfter(rent.getEndDate());

        List<Rent> all = rentRepository.findAll();
        assertThat(all).hasSize(1);

        rentRepository.deleteById(rent.getId());
        assertThat(rentRepository.findAll()).isEmpty();
    }

    @Test
    @Order(4)
    void testRentLogic(){
        Rent rent = new Rent(LocalDate.now(), LocalDate.now().plusDays(3), testClient, testHouse);
        rentRepository.saveRent(rent);

        Rent rent2 = new Rent(LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), testClient, testHouse);

        try {
            rentRepository.saveRent(rent2);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(HouseNotAvaibleException.class);
        }
    }



}
