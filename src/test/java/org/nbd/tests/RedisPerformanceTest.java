package org.nbd.tests;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.nbd.config.MongoConfig;
import org.nbd.config.RedisConfig;
import org.nbd.decorators.ClientRepoCacheDecorator;
import org.nbd.model.Client;
import org.nbd.model.ClientType;
import org.nbd.model.Default;
import org.nbd.repositories.ClientRepo;
import org.nbd.repositories.ClientTypeRepo;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedisPerformanceTest {

    private ClientRepo clientRepo;
    private ClientRepoCacheDecorator clientCache;
    private ClientType defaultType;
    private List<ObjectId> clientIds;
    private static final int TEST_DATA_SIZE = 50;
    private static final int READ_ITERATIONS = 100;

    @BeforeAll
    void setup() {
        MongoConfig config = new MongoConfig();


        clientRepo = new ClientRepo(config.getDatabase());
        ClientTypeRepo clientTypeRepo = new ClientTypeRepo(config.getDatabase());

        clientRepo.deleteAll();
        clientTypeRepo.deleteAll();

        clientCache = new ClientRepoCacheDecorator(clientRepo);

        defaultType = new Default();
        clientTypeRepo.save(defaultType);

        clientIds = new ArrayList<>();
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            Client client = Client.builder()
                    .firstName("Client" + i)
                    .lastName("Test")
                    .phoneNumber("123-456-" + i)
                    .clientType(defaultType)
                    .active(true)
                    .build();
            clientRepo.save(client);
            clientIds.add(client.getId());
        }
    }

    @Test
    void testCachedReadIsFasterThanMongoRead() {
        for (ObjectId id : clientIds) {
            clientCache.findById(id);
        }

        long mongoStartTime = System.nanoTime();
        for (int i = 0; i < READ_ITERATIONS; i++) {
            for (ObjectId id : clientIds) {
                Client client = clientRepo.findById(id);
                assertNotNull(client);
            }
        }
        long mongoEndTime = System.nanoTime();
        long mongoDuration = TimeUnit.NANOSECONDS.toNanos(mongoEndTime - mongoStartTime);

        long redisStartTime = System.nanoTime();
        for (int i = 0; i < READ_ITERATIONS; i++) {
            for (ObjectId id : clientIds) {
                Client client = clientCache.findById(id);
                assertNotNull(client);
            }
        }
        long redisEndTime = System.nanoTime();
        long redisDuration = TimeUnit.NANOSECONDS.toNanos(redisEndTime - redisStartTime);


        assertTrue(redisDuration < mongoDuration);
    }

    @Test
    void testSingleRecordCachedReadIsFaster() {
        ObjectId testId = clientIds.get(0);
        clientCache.findById(testId);

        long mongoStartTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            Client client = clientRepo.findById(testId);
            assertNotNull(client);
        }
        long mongoEndTime = System.nanoTime();
        long mongoDuration = TimeUnit.NANOSECONDS.toNanos(mongoEndTime - mongoStartTime);

        long redisStartTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            Client client = clientCache.findById(testId);
            assertNotNull(client);
        }
        long redisEndTime = System.nanoTime();
        long redisDuration = TimeUnit.NANOSECONDS.toNanos(redisEndTime - redisStartTime);

        assertTrue(redisDuration < mongoDuration);
    }

    @Test
    void testConcurrentCachedReadsAreFaster() throws InterruptedException {
        for (ObjectId id : clientIds) {
            clientCache.findById(id);
        }

        int threadCount = 5;
        int readsPerThread = 100;

        long mongoStartTime = System.nanoTime();
        List<Thread> mongoThreads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < readsPerThread; j++) {
                    for (ObjectId id : clientIds) {
                        clientRepo.findById(id);
                    }
                }
            });
            mongoThreads.add(thread);
            thread.start();
        }
        for (Thread thread : mongoThreads) {
            thread.join();
        }
        long mongoEndTime = System.nanoTime();
        long mongoDuration = TimeUnit.NANOSECONDS.toNanos(mongoEndTime - mongoStartTime);

        long redisStartTime = System.nanoTime();
        List<Thread> redisThreads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < readsPerThread; j++) {
                    for (ObjectId id : clientIds) {
                        clientCache.findById(id);
                    }
                }
            });
            redisThreads.add(thread);
            thread.start();
        }
        for (Thread thread : redisThreads) {
            thread.join();
        }
        long redisEndTime = System.nanoTime();
        long redisDuration = TimeUnit.NANOSECONDS.toNanos(redisEndTime - redisStartTime);

        assertTrue(redisDuration < mongoDuration);
    }


}