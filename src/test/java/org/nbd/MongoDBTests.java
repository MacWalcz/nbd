package org.nbd;

import org.junit.jupiter.api.*;
import org.nbd.model.*;
import org.nbd.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RepositoryClusterIntegrationTest {

    @Autowired
    private ClientRepo clientRepository;

    @Autowired
    private HouseRepo houseRepository;

    @Autowired
    private RentRepo rentRepository;

    private Client testClient;
    private House testHouse;

    @BeforeEach
    void setup() {
        clientRepository.deleteAll();
        houseRepository.deleteAll();
        rentRepository.deleteAll();

        testClient = Client.builder()
                .id(UUID.randomUUID())
                .login("karabeika")
                .firstName("Mikita")
                .lastName("Test")
                .phoneNumeber("505050505")
                .clientType(new Default())
                .active(true)
                .build();

        testHouse = new House("H1", 200.0, 50.0);
        testHouse.setId(UUID.randomUUID());

        clientRepository.save(testClient);
        houseRepository.save(testHouse);
    }

    @Test
    @Order(1)
    void testClientCRUD() {
        // CREATE
        Client client = Client.builder()
                .id(UUID.randomUUID())
                .login("user1")
                .firstName("Jan")
                .lastName("Kowalski")
                .phoneNumeber("123456789")
                .clientType(new Default())
                .build();

        clientRepository.save(client);

        // READ
        Optional<Client> found = clientRepository.findById(client.getId().toString());
        assertThat(found).isPresent();

        // UPDATE
        found.get().setLastName("Nowak");
        clientRepository.save(found.get());
        Client updated = clientRepository.findById(client.getId().toString()).orElseThrow();
        assertThat(updated.getLastName()).isEqualTo("Nowak");

        // DELETE
        clientRepository.deleteById(client.getId().toString());
        assertThat(clientRepository.findById(client.getId().toString())).isEmpty();
    }

    @Test
    @Order(2)
    void testHouseCRUD() {
        House house = new House("H2", 300.0, 80.0);
        house.setId(UUID.randomUUID());
        houseRepository.save(house);

        House found = houseRepository.findById(house.getId().toString()).orElse(null);
        assertThat(found).isNotNull();

        found.setPrice(350.0);
        houseRepository.save(found);

        House updated = houseRepository.findById(house.getId().toString()).orElse(null);
        assertThat(updated.getPrice()).isEqualTo(350.0);

        houseRepository.deleteById(house.getId().toString());
        assertThat(houseRepository.findById(house.getId().toString())).isEmpty();
    }

    @Test
    @Order(3)
    void testRentCRUD() {
        Rent rent = new Rent(LocalDate.now(), LocalDate.now().plusDays(3), testClient, testHouse);
        rent.setId(UUID.randomUUID());
        rentRepository.save(rent);

        Rent found = rentRepository.findById(rent.getId().toString()).orElse(null);
        assertThat(found).isNotNull();

        found.setEndDate(LocalDate.now().plusDays(5));
        rentRepository.save(found);
        Rent updated = rentRepository.findById(rent.getId().toString()).orElse(null);
        assertThat(updated.getEndDate()).isAfter(rent.getEndDate());

        List<Rent> all = rentRepository.findAll();
        assertThat(all).hasSize(1);

        rentRepository.deleteById(rent.getId().toString());
        assertThat(rentRepository.findAll()).isEmpty();
    }


}
