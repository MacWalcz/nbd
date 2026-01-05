package org.nbd.app;

import org.bson.types.ObjectId;
import org.nbd.config.MongoConfig;
import org.nbd.config.KafkaConfig;
import org.nbd.decorators.RentRepoCacheDecorator;
import org.nbd.kafka.RentProducer;
import org.nbd.model.Client;
import org.nbd.model.Default;
import org.nbd.model.House;
import org.nbd.model.Rent;
import org.nbd.repositories.RentRepo;

import java.time.LocalDate;

public class ProducerApp {

    private static final String RENTAL_AGENCY_NAME = "NBD Rental Agency";

    public static void main(String[] args) throws InterruptedException {
        KafkaConfig.createTopic();
        MongoConfig mongoConfig = new MongoConfig();
        RentRepo rentRepo = new RentRepo(mongoConfig.getDatabase());
        RentProducer producer = new RentProducer(RENTAL_AGENCY_NAME);

        RentRepoCacheDecorator rentRepoDecorator = new RentRepoCacheDecorator(rentRepo, producer);

        System.out.println("Aplikacja Producenta uruchomiona. Symulacja tworzenia wypożyczeń...");

        House house = House.builder()
                .id(new ObjectId())
                .houseNumber("H1")
                .price(100)
                .area(50)
                .build();
        Client client = Client.builder()
                .id(new ObjectId())
                .firstName("Jan")
                .lastName("Kowalski")
                .clientType(new Default())
                .build();

        Rent rent1 = Rent.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .house(house)
                .client(client)
                .cost(500)
                .build();

        rentRepoDecorator.save(rent1);
        System.out.println("Utworzono wypożyczenie 1 i wysłano event do Kafka.");

        Thread.sleep(1000);

        Rent rent2 = Rent.builder()
                .startDate(LocalDate.now().plusDays(10))
                .endDate(LocalDate.now().plusDays(15))
                .house(house)
                .client(client)
                .cost(500)
                .build();

        rentRepoDecorator.save(rent2);
        System.out.println("Utworzono wypożyczenie 2 i wysłano event do Kafka.");

        producer.close();
        System.out.println("Aplikacja Producenta zakończyła działanie.");
    }
}