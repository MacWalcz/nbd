package org.nbd.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.nbd.config.JsonUtil;
import org.nbd.config.KafkaConfig;
import org.nbd.model.Rent;
import org.nbd.model.RentEvent;

import java.util.Properties;
import java.util.concurrent.Future;

public class RentProducer {

    private final KafkaProducer<String, String> producer;
    private final String rentalAgencyName;

    public RentProducer(String rentalAgencyName) {
        this.rentalAgencyName = rentalAgencyName;
        Properties props = KafkaConfig.getCommonProps();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        this.producer = new KafkaProducer<>(props);
    }

    public Future<?> sendRentEvent(Rent rent) {
        RentEvent event = RentEvent.builder()
                .rent(rent)
                .rentalAgencyName(this.rentalAgencyName)
                .timestamp(System.currentTimeMillis())
                .build();

        String key = rent.getId().toHexString();
        String jsonValue = JsonUtil.toJson(event);

        ProducerRecord<String, String> record = new ProducerRecord<>(
                KafkaConfig.RENT_TOPIC,
                key,
                jsonValue
        );

        return producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.printf(">>> PRODUCENT: Wysłano rezerwację %s. Partycja: %d, Offset: %d%n",
                        key, metadata.partition(), metadata.offset());
            } else {
                System.err.println("!!! PRODUCENT: Błąd podczas wysyłania rezerwacji: " + exception.getMessage());
            }
        });
    }

    public void close() {
        producer.close();
    }
}