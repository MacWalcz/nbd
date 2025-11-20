package org.nbd.tests;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.nbd.config.MongoConfig;
import org.nbd.config.RedisConfig;
import org.nbd.decorators.ClientTypeRepoCacheDecorator;
import org.nbd.model.*;
import org.nbd.repositories.*;
import org.nbd.decorators.ClientRepoCacheDecorator;
import org.nbd.decorators.HouseRepoCacheDecorator;
import redis.clients.jedis.Jedis;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedisCacheTests {

    private ClientRepo clientRepo;
    private HouseRepo houseRepo;

    private ClientRepoCacheDecorator clientCache;
    private HouseRepoCacheDecorator houseCache;
    private ClientTypeRepoCacheDecorator clientTypeCache;

    private ClientType defaultType;
    private ClientType premiumType;
    private ClientType luxuryType;

    @BeforeAll
    void setup() {
        MongoConfig config = new MongoConfig();

        clientRepo = new ClientRepo(config.getDatabase());
        houseRepo = new HouseRepo(config.getDatabase());
        ClientTypeRepo clientTypeRepo = new ClientTypeRepo(config.getDatabase());

        clientRepo.deleteAll();
        houseRepo.deleteAll();
        clientTypeRepo.deleteAll();

        clientCache = new ClientRepoCacheDecorator(clientRepo);
        houseCache = new HouseRepoCacheDecorator(houseRepo);
        clientTypeCache = new ClientTypeRepoCacheDecorator(clientTypeRepo);

        defaultType = new Default();
        premiumType = new Premium();
        luxuryType = new Luxury();

        clientTypeCache.save(defaultType);
        clientTypeCache.save(premiumType);
        clientTypeCache.save(luxuryType);

    }

    private Client createClient(String name, ClientType type) {
        Client c = Client.builder()
                .firstName(name)
                .lastName("Test")
                .phoneNumber("123")
                .clientType(type)
                .active(true)
                .build();
        clientCache.save(c);
        return c;
    }

    private House createHouse(String number) {
        House h = House.builder()
                .houseNumber(number)
                .area(50)
                .price(100)
                .build();
        houseCache.save(h);
        return h;
    }

    @Test
    void testClientCacheCRUD() {
        Client c = createClient("Jan", defaultType);
        ObjectId id = c.getId();
        assertNotNull(id);

        Client found = clientCache.findById(id);
        assertEquals("Jan", found.getFirstName());


        found.setLastName("Kowalski");
        clientCache.update(id, found);

        Client updated = clientCache.findById(id);
        assertEquals("Kowalski", updated.getLastName());
    }

    @Test
    void testHouseCacheCRUD() {
        House h = createHouse("A1");
        ObjectId id = h.getId();
        assertNotNull(id);

        House found = houseCache.findById(id);
        assertEquals("A1", found.getHouseNumber());



        found.setArea(99);
        houseCache.update(id, found);


        House updated = houseCache.findById(id);
        assertEquals(99, updated.getArea());
    }

    @Test
    void testInvalidateCacheOnDelete() {
        Client c = createClient("Ola", premiumType);
        ObjectId id = c.getId();

        clientCache.findById(id);

        clientCache.deleteById(id);


        assertNull(clientCache.findById(id));
    }
}

