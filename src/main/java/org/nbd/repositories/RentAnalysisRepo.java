package org.nbd.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.nbd.model.RentEvent;

public class RentAnalysisRepo {
    private final MongoCollection<RentEvent> collection;

    public RentAnalysisRepo(MongoDatabase database) {
        this.collection = database.getCollection("rents_analysis", RentEvent.class);
    }

    public void save(RentEvent event) {
        collection.insertOne(event);
        System.out.println("<<< KONSUMENT: Zapisano event rezerwacji do bazy analitycznej: " + event.getRent().getId().toHexString());
    }

    public long count() {
        return collection.countDocuments();
    }
}