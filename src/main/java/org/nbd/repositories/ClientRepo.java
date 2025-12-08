package org.nbd.repositories;

import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.config.MongoConfig;
import org.nbd.model.Client;
import org.nbd.repositories.BaseRepo;
import org.nbd.repositories.RepoManager;

import java.util.ArrayList;
import java.util.List;

public class ClientRepo extends BaseRepo<Client> implements RepoManager<Client> {

    public ClientRepo(MongoConfig config) {
        super(config, "clients", Client.class);
    }

    @Override
    public Client save(Client c) {
        collection.insertOne(c);
        return c;
    }

    @Override
    public Client findById(ObjectId id) {
        return collection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public void update(ObjectId id, Client c) {
        collection.replaceOne(Filters.eq("_id", id), c);
    }

    @Override
    public void deleteById(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    @Override
    public void deleteAll() {
        collection.deleteMany(Filters.exists("_id"));
    }

    @Override
    public List<Client> findAll() {
        return collection.find().into(new ArrayList<>());
    }

    public Client findByLogin(String login) {
        return collection.find(Filters.eq("login", login)).first();
    }

    public boolean existsByLogin(String login) {
        return collection.find(Filters.eq("login", login)).first() != null;
    }
}
