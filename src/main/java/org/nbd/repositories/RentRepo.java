package org.nbd.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.nbd.exceptions.HouseNotAvaibleException;
import org.nbd.model.House;
import org.nbd.model.Rent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

@ApplicationScoped
public class RentRepo implements RepoManager<Rent> {

    private final MongoDatabase database;
    private final MongoCollection<Rent> collection;

    public RentRepo() {
        this.database = null;
        this.collection = null;
    }

    @Inject
    public RentRepo(MongoDatabase database) {
        this.database = database;
        this.collection = database.getCollection("rents", Rent.class);
    }

    public Rent save(Rent rent) {

        List<Rent> overlaps = findOverlappingReservations(
                rent.getHouse(),
                rent.getStartDate(),
                rent.getEndDate()
        );

        if (!overlaps.isEmpty()) {
            throw new HouseNotAvaibleException(rent.getHouse().getId());
        }

        collection.insertOne(rent);
        return rent;
    }


    public List<Rent> findOverlappingReservations(House houseId, LocalDate start, LocalDate end) {

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

    public Rent update(ObjectId id, Rent updated) {
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

    public boolean existsActiveForHouse(ObjectId houseId) {
        Document filter = new Document()
                .append("house._id", houseId)
                .append("endDate", null);

        return collection.find(filter).first() != null;
    }

    public List<Rent> findByClientIdAndEndDateIsNull(ObjectId clientId) {
        List<Rent> result = new ArrayList<>();
        for (Rent r : collection.find(and(eq("client._id", clientId), eq("endDate", null)))) {
            result.add(r);
        }
        return result;
    }

    public List<Rent> findByClientIdAndEndDateIsNotNull(ObjectId clientId) {
        List<Rent> result = new ArrayList<>();
        for (Rent r : collection.find(and(eq("client._id", clientId), ne("endDate", null)))) {
            result.add(r);
        }
        return result;
    }

    public List<Rent> findByHouseIdAndEndDateIsNull(ObjectId houseId) {
        List<Rent> result = new ArrayList<>();
        for (Rent r : collection.find(and(eq("house._id", houseId), eq("endDate", null)))) {
            result.add(r);
        }
        return result;
    }

    public List<Rent> findByHouseIdAndEndDateIsNotNull(ObjectId houseId) {
        List<Rent> result = new ArrayList<>();
        for (Rent r : collection.find(and(eq("house._id", houseId), ne("endDate", null)))) {
            result.add(r);
        }
        return result;
    }

}