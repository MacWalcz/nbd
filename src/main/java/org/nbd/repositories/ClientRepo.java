package org.nbd.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.nbd.model.Client;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ClientRepo implements RepoManager<Client> {

    private final MongoCollection<Client> collection;

    public ClientRepo(MongoDatabase database) {
        this.collection = database
                .getCollection("clients", Client.class);

    }

    public void save(Client client) {
        collection.insertOne(client);
    }

    public Client findById(ObjectId id) {
        return collection.find(eq("_id", id)).first();
    }

    public List<Client> findAll() {
        List<Client> list = new ArrayList<>();
        for (Client c : collection.find()) {
            list.add(c);
        }
        return list;
    }

    public void update(ObjectId id, Client updated) {
        collection.replaceOne(eq("_id", id), updated);
    }

    public void deleteById(ObjectId id) {
        collection.deleteOne(eq("_id", id));
    }

    public void deleteAll() {
        collection.deleteMany(new Document());
    }
}
