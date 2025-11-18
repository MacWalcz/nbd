package org.nbd.tests;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.nbd.config.MongoConfig;
import org.nbd.model.*;
import org.nbd.repositories.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MongoCRUDTests {

    private ClientRepo clientRepo;
    private HouseRepo houseRepo;
    private RentRepo rentRepo;
    private ClientTypeRepo clientTypeRepo;

    private ClientType defaultType;
    private ClientType premiumType;
    private ClientType luxuryType;

    @BeforeAll
    void setup() {
        MongoConfig config = new MongoConfig();

        clientRepo = new ClientRepo(config.getDatabase());
        houseRepo = new HouseRepo(config.getDatabase());
        rentRepo = new RentRepo(config.getDatabase());
        clientTypeRepo = new ClientTypeRepo(config.getDatabase());

        rentRepo.deleteAll();
        clientRepo.deleteAll();
        houseRepo.deleteAll();
        clientTypeRepo.deleteAll();

        defaultType = new Default();
        premiumType = new Premium();
        luxuryType = new Luxury();

        clientTypeRepo.save(defaultType);
        clientTypeRepo.save(premiumType);
        clientTypeRepo.save(luxuryType);
    }

    private Client createClient(String name, ClientType type) {
        Client c = Client.builder()
                .firstName(name)
                .lastName("Test")
                .phoneNumber("123")
                .clientType(type)
                .active(true)
                .build();
        clientRepo.save(c);
        return c;
    }

    private House createHouse(String number) {
        House h = House.builder()
                .houseNumber(number)
                .area(50)
                .price(100)
                .build();
        houseRepo.save(h);
        return h;
    }

    @Test
    void testClientCRUD() {
        Client c = createClient("Jan", defaultType);
        assertNotNull(c.getId());

        Client found = clientRepo.findById(c.getId());
        assertEquals("Jan", found.getFirstName());

        found.setLastName("Kowalski");
        clientRepo.update(found.getId(), found);

        Client updated = clientRepo.findById(found.getId());
        assertEquals("Kowalski", updated.getLastName());

        clientRepo.deleteById(found.getId());
        assertNull(clientRepo.findById(found.getId()));
    }

    @Test
    void testHouseCRUD() {
        House h = createHouse("A1");
        assertNotNull(h.getId());

        House found = houseRepo.findById(h.getId());
        assertEquals("A1", found.getHouseNumber());

        found.setArea(99);
        houseRepo.update(found.getId(), found);

        House updated = houseRepo.findById(h.getId());
        assertEquals(99, updated.getArea());

        houseRepo.deleteById(h.getId());
        assertNull(houseRepo.findById(h.getId()));
    }

    @Test
    void testRentCRUD() {
        Client c = createClient("Ola", premiumType);
        House h = createHouse("B2");

        Rent rent = Rent.builder()
                .client(c)
                .house(h)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(3))
                .build();

        rentRepo.save(rent);
        assertNotNull(rent.getId());

        Rent found = rentRepo.findById(rent.getId());
        assertEquals(c.getId(), found.getClient().getId());

        found.setCost(999);
        rentRepo.update(found.getId(), found);

        Rent updated = rentRepo.findById(found.getId());
        assertEquals(999, updated.getCost());

        rentRepo.deleteById(found.getId());
        assertNull(rentRepo.findById(found.getId()));
    }

    @Test
    void testCannotRentAlreadyOccupiedHouse() {
        Client c1 = createClient("A", defaultType);
        Client c2 = createClient("B", premiumType);
        House h = createHouse("C3");

        LocalDate start = LocalDate.of(2025, 1, 10);
        LocalDate end = LocalDate.of(2025, 1, 20);

        Rent r1 = Rent.builder()
                .client(c1)
                .house(h)
                .startDate(start)
                .endDate(end)
                .build();
        rentRepo.save(r1);

        Rent r2 = Rent.builder()
                .client(c2)
                .house(h)
                .startDate(start.plusDays(2))
                .endDate(start.plusDays(5))
                .build();

        assertThrows(RuntimeException.class, () -> {
            rentRepo.save(r2);
        });
    }
}