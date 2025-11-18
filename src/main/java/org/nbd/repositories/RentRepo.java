package org.nbd.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.nbd.exceptions.HouseNotAvaibleException;
import org.nbd.model.Client;
import org.nbd.model.Rent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class RentRepo implements RepoManager<Rent> {

    private final MongoCollection<Rent> collection;

    public RentRepo(MongoDatabase db) {
        this.collection = db.getCollection("rents", Rent.class);
    }

    public void save(Rent rent) {

        List<Rent> overlaps = findOverlappingReservations(
                rent.getHouse().getId(),
                rent.getStartDate(),
                rent.getEndDate()
        );

        if (!overlaps.isEmpty()) {
            throw new HouseNotAvaibleException("Chata zajęta!");
        }

        collection.insertOne(rent);
    }


    public List<Rent> findOverlappingReservations(ObjectId houseId, LocalDate start, LocalDate end) {

        Document filter = new Document()
                .append("house._id", houseId)
                .append("startDate", new Document("$lte", end))
                .append("endDate", new Document("$gte", start));

        List<Rent> result = new ArrayList<>();
        for (Rent r : collection.find(filter)) {
            result.add(r);
        }
        return result;
    }


    public Rent findById(ObjectId id) {
        return collection.find(eq("_id", id)).first();
    }

    public List<Rent> findAll() {
        List<Rent> list = new ArrayList<>();
        for (Rent r : collection.find()) list.add(r);
        return list;
    }

    public void update(ObjectId id, Rent updated) {
        collection.replaceOne(eq("_id", id), updated);
    }

    public void deleteById(ObjectId id) {
        collection.deleteOne(eq("_id", id));
    }

    public void deleteAll() {
        collection.deleteMany(new Document());
    }
}
