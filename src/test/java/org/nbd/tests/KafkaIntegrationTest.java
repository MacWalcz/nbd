package org.nbd.tests;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.nbd.config.KafkaConfig;
import org.nbd.config.MongoConfig;
import org.nbd.kafka.RentConsumer;
import org.nbd.kafka.RentProducer;
import org.nbd.model.*;
import org.nbd.repositories.RentAnalysisRepo;
import org.nbd.repositories.RentRepo;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class KafkaIntegrationTest {

    private RentAnalysisRepo analysisRepo;
    private RentRepo rentRepo;
    private RentProducer producer;
    private MongoConfig mongoConfig;

    @BeforeAll
    void setup() {
        mongoConfig = new MongoConfig();
        rentRepo = new RentRepo(mongoConfig.getDatabase());
        analysisRepo = new RentAnalysisRepo(mongoConfig.getDatabase());

        mongoConfig.getDatabase().getCollection("rents_analysis").drop();
        rentRepo.deleteAll();

        KafkaConfig.createTopic();
        producer = new RentProducer("Test-Agency");
    }

    @AfterAll
    void tearDown() {
        producer.close();
    }

    @Test
    @Order(1)
    @DisplayName("1. Podział partycji między dwóch konsumentów")
    void testTwoConsumersPartitionAssignment() throws InterruptedException {
        RentConsumer consumer1 = new RentConsumer("Konsument-1", analysisRepo, 1);
        RentConsumer consumer2 = new RentConsumer("Konsument-2", analysisRepo, 1);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(consumer1);
        executor.submit(consumer2);

        Thread.sleep(2000);

        int totalMessages = 10;
        for (int i = 0; i < totalMessages; i++) {
            Rent r = createSampleRent("House-" + i);
            producer.sendRentEvent(r);
        }

        consumer1.waitForCompletion(15);
        consumer2.waitForCompletion(15);

        Thread.sleep(5000);

        long count = analysisRepo.count();
        System.out.println(">>> Finałowy stan bazy w Test 1: " + count);

        assertEquals(10, count, "Baza rents_analysis powinna zawierać dokładnie 10 rekordów");

        consumer1.stop();
        consumer2.stop();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    @Order(2)
    @DisplayName("2. Sprawdzenie braku duplikatów po restarcie")
    void testNoDuplicateProcessingAfterRestart() throws InterruptedException {

        long countBefore = analysisRepo.count();

        RentConsumer consumerRestarted = new RentConsumer("Konsument-Restart", analysisRepo, 0);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(consumerRestarted);

        Thread.sleep(5000);
        long countAfter = analysisRepo.count();

        assertEquals(countBefore, countAfter, "Wykryto duplikaty! Konsument przeczytał stare wiadomości.");

        producer.sendRentEvent(createSampleRent("After-Restart"));
        Thread.sleep(3000);

        assertEquals(countBefore + 1, analysisRepo.count(), "Nowa wiadomość powinna być zapisana");

        consumerRestarted.stop();
        executor.shutdown();
    }

    private Rent createSampleRent(String houseNumber) {
        return Rent.builder()
                .id(new ObjectId())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .house(House.builder().id(new ObjectId()).houseNumber(houseNumber).price(100).build())
                .client(Client.builder().id(new ObjectId()).firstName("Jan").clientType(new Default()).build())
                .cost(500)
                .build();
    }
}