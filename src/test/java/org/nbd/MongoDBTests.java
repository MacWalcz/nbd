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
                .phoneNumber("505050505")
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

}
