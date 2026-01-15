package org.nbd.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaConfig {
    public static final String RENT_TOPIC = "wypozyczenia_rezerwacje";
    public static final int PARTITIONS = 3;
    public static final short REPLICATION_FACTOR = 3;

    public static Properties getCommonProps() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        return props;
    }

    public static void createTopic() {
        Properties adminProps = getCommonProps();
        try (AdminClient adminClient = AdminClient.create(adminProps)) {
            NewTopic newTopic = new NewTopic(RENT_TOPIC, PARTITIONS, REPLICATION_FACTOR);
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
            System.out.println("Utworzono temat: " + RENT_TOPIC + " z " + PARTITIONS + " partycjami.");
        } catch (InterruptedException | ExecutionException e) {
            if (e.getCause() != null && e.getCause().getMessage().contains("TopicExistsException")) {
                System.out.println("Temat " + RENT_TOPIC + " już istnieje.");
            } else {
                System.err.println("Błąd podczas tworzenia tematu: " + e.getMessage());
            }
        }
    }
}