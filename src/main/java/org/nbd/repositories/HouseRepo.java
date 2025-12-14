package org.nbd.repositories;

import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.nbd.model.House;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
public class HouseRepo extends BaseRepo<House> implements RepoManager<House> {


    public HouseRepo() {
        super();
    }

    @Inject
    public HouseRepo(MongoDatabase database) {
        super(database);
    }

    public House save(House house) {
        collection.insertOne(house);
        return house;
    }

    public House findById(ObjectId id) {
        return collection.find(eq("_id", id)).first();
    }

    public List<House> findAll() {
        return collection.find().into(new ArrayList<>());
    }

    public House update(ObjectId id, House updated) {
        updated.setId(id);
        collection.replaceOne(eq("_id", id), updated);
        return updated;
    }

    public void deleteById(ObjectId id) {
        collection.deleteOne(eq("_id", id));
    }

    public void deleteAll() {
        collection.deleteMany(new Document());
    }
}