package org.nbd.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.nbd.model.Client;
import org.nbd.model.ClientType;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ClientTypeRepo implements RepoManager<ClientType> {

    private final MongoCollection<ClientType> collection;

    public ClientTypeRepo(MongoDatabase db) {
        this.collection = db.getCollection("clientTypes", ClientType.class);
    }

    public void save(ClientType clientType) {
        collection.insertOne(clientType);
    }

    public ClientType findById(ObjectId id) {
        return collection.find(eq("_id", id)).first();
    }

    public List<ClientType> findAll() {
        List<ClientType> list = new ArrayList<>();
        for (ClientType t : collection.find()) {
            list.add(t);
        }
        return list;
    }

    public void update(ObjectId id, ClientType updated) {
        collection.replaceOne(eq("_id", id), updated);
    }

    public void deleteById(ObjectId id) {
        collection.deleteOne(eq("_id", id));
    }

    public void deleteAll() {
        collection.deleteMany(new Document());
    }
}
