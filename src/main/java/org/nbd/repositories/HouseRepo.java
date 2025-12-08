package org.nbd.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.nbd.model.Client;
import org.nbd.model.House;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
public class HouseRepo implements RepoManager<House> {

    private MongoCollection<House> collection;

    public HouseRepo() {}

    @Inject
    public HouseRepo(MongoDatabase db) {
        this.collection = db.getCollection("houses", House.class);
    }

    public House save(House house) {
        collection.insertOne(house);
        return house;
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