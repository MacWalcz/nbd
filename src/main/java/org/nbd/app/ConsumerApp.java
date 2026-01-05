package org.nbd.app;

import org.nbd.config.MongoConfig;
import org.nbd.kafka.RentConsumer;
import org.nbd.repositories.RentAnalysisRepo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConsumerApp {

    public static void main(String[] args) {
        String consumerId = args[0];

        MongoConfig mongoConfig = new MongoConfig();
        RentAnalysisRepo rentAnalysisRepo = new RentAnalysisRepo(mongoConfig.getDatabase());

        RentConsumer consumer = new RentConsumer(consumerId, rentAnalysisRepo, 0);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(consumer);

        System.out.println("Konsument ID: " + consumerId + " został uruchomiony.");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nZamykanie konsumenta " + consumerId + "...");
            consumer.stop();
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Wątek konsumenta nie zamknął się w czasie.");
                }
            } catch (InterruptedException ignored) {}
        }));
    }
}