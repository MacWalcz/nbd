package org.nbd.kafka;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import java.util.Collection;

public class ConsumerRebalanceListenerImpl implements ConsumerRebalanceListener {

    private final String consumerId;

    public ConsumerRebalanceListenerImpl(String consumerId) {
        this.consumerId = consumerId;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        System.out.printf("[KONSUMENT %s] --- Utracono partycje: %s%n", consumerId, partitions);
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        System.out.printf("[KONSUMENT %s] *** Przypisano partycje: %s ***%n", consumerId, partitions);
    }
}