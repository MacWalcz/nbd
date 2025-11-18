package org.nbd.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.nbd.model.Client;
import org.nbd.model.House;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class HouseRepo implements RepoManager<House> {

    private final MongoCollection<House> collection;

    public HouseRepo(MongoDatabase db) {
        this.collection = db.getCollection("houses", House.class);
    }

    public void save(House house) {
        collection.insertOne(house);
    }

    public House findById(ObjectId id) {
        return collection.find(eq("_id", id)).first();
    }

    public List<House> findAll() {
        List<House> list = new ArrayList<>();
        for (House h : collection.find()) {
            list.add(h);
        }
        return list;
    }

    public void update(ObjectId id, House updated) {
        collection.replaceOne(eq("_id", id), updated);
    }

    public void deleteById(ObjectId id) {
        collection.deleteOne(eq("_id", id));
    }

    public void deleteAll() {
        collection.deleteMany(new Document());
    }
}
