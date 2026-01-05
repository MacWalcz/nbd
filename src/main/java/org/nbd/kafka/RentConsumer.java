package org.nbd.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.nbd.config.JsonUtil;
import org.nbd.config.KafkaConfig;
import org.nbd.model.RentEvent;
import org.nbd.repositories.RentAnalysisRepo;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RentConsumer implements Runnable {

    private final KafkaConsumer<String, String> consumer;
    private final RentAnalysisRepo rentAnalysisRepo;
    private final String consumerId;
    private final CountDownLatch latch;

    public RentConsumer(String consumerId, RentAnalysisRepo rentAnalysisRepo, int expectedMessages) {
        this.consumerId = consumerId;
        this.rentAnalysisRepo = rentAnalysisRepo;
        this.latch = new CountDownLatch(expectedMessages);

        Properties props = KafkaConfig.getCommonProps();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "rent-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Dokładnie raz
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Collections.singletonList(KafkaConfig.RENT_TOPIC), new ConsumerRebalanceListenerImpl(consumerId));
    }

    @Override
    public void run() {
        System.out.println("[KONSUMENT " + consumerId + "] Uruchomiony, oczekiwanie na partycje...");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                if (records.isEmpty()) continue;

                for (ConsumerRecord<String, String> record : records) {
                    try {
                        RentEvent rentEvent = JsonUtil.fromJson(record.value(), RentEvent.class);

                        System.out.printf("[KONSUMENT %s] Odebrano Part: %d, Offset: %d%n",
                                consumerId, record.partition(), record.offset());

                        // Zapis do Bazy Danych
                        rentAnalysisRepo.save(rentEvent);
                        latch.countDown();

                    } catch (Exception e) {
                        System.err.println("!!! KONSUMENT: Błąd przetwarzania wiadomości: " + e.getMessage());
                    }
                }

                consumer.commitSync();
            }
        } catch (Exception e) {
            System.err.println("!!! KONSUMENT: Nieoczekiwany błąd: " + e.getMessage());
        } finally {
            consumer.close();
            System.out.println("[KONSUMENT " + consumerId + "] Zamknięty.");
        }
    }

    public boolean waitForCompletion(long timeoutSeconds) throws InterruptedException {
        return this.latch.await(timeoutSeconds, TimeUnit.SECONDS);
    }

    public void stop() {
        this.consumer.wakeup();
    }
}