package org.nbd.repositories;

import com.mongodb.MongoCommandException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.nbd.model.*;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserRepo extends BaseRepo<User> implements RepoManager<User> {

    public UserRepo() {
        super();
    }

    @Inject
    public UserRepo(MongoDatabase database) {
        super(database);
    }

    @PostConstruct
    public void init() {

        try {

            collection.createIndex(Indexes.ascending("login"),
                    new IndexOptions().unique(true));
            System.out.println("Utworzono unikalny indeks dla pola 'login' w UserRepo.");
        } catch (MongoCommandException e) {

            if (!e.getErrorCodeName().equals("IndexKeySpecsConflict") && !e.getErrorCodeName().equals("IndexAlreadyExists")) {
                throw e;

            }
        }
    }

    @Override
    public User save(User u) {
        collection.insertOne(u);
        return u;
    }

    @Override
    public User findById(ObjectId id) {
        return collection.find(Filters.eq("_id", id)).first();
    }

    public Client findClientById(ObjectId id) {
        return database
                .getCollection("users", Client.class)
                .find(Filters.eq("_id", id))
                .first();
    }


    @Override
    public User update(ObjectId id, User updated) {
        updated.setId(id);
        collection.replaceOne(Filters.eq("_id", id), updated);
        return updated;
    }

    public Client updateClient(ObjectId id, Client updated) {
        updated.setId(id);
        collection.replaceOne(Filters.eq("_id", id), updated);
        return updated;
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
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        users.addAll(findAdministrators());
        users.addAll(findEmployees());
        users.addAll(findClients());
        return users;
    }

    public User findByLogin(String login) {
        return collection.find(Filters.eq("login", login)).first();
    }

    public boolean existsByLogin(String login) {
        return collection.find(Filters.eq("login", login)).first() != null;
    }

    public List<User> findByLoginPartial(String partial) {
        List<User> result = new ArrayList<>();
        String lowerPartial = partial.toLowerCase();

        for (User u : findAll()) {
            if (u.getLogin() != null && u.getLogin().toLowerCase().contains(lowerPartial)) {
                result.add(u);
            }
        }

        return result;
    }


    public List<Administrator> findAdministrators() {
        return database
                .getCollection("users", Administrator.class)
                .find(Filters.eq("_class", "administrator"))
                .into(new ArrayList<>());
    }

    public List<Employee> findEmployees() {
        return database
                .getCollection("users", Employee.class)
                .find(Filters.eq("_class", "employee"))
                .into(new ArrayList<>());
    }

    public List<Client> findClients() {
        return database
                .getCollection("users", Client.class)
                .find(Filters.eq("_class", "client"))
                .into(new ArrayList<>());
    }

    public List<User> findAllByLoginContainingIgnoreCase(String partial) {
        List<User> result = new ArrayList<>();
        String lowerPartial = partial.toLowerCase();

        List<User> allUsers = findAll();
        for (User u : allUsers) {
            if (u.getLogin() != null && u.getLogin().toLowerCase().contains(lowerPartial)) {
                result.add(u);
            }
        }

        return result;
    }

    public List<Client> findAllClientsByLoginContainingIgnoreCase(String partial) {
        List<Client> result = new ArrayList<>();
        String lowerPartial = partial.toLowerCase();

        List<Client> allUsers = findClients();
        for (Client u : allUsers) {
            if (u.getLogin() != null && u.getLogin().toLowerCase().contains(lowerPartial)) {
                result.add(u);
            }
        }

        return result;
    }

    public Client findClientByLogin(String login) {

        return database
                .getCollection("users", Client.class)
                .find(Filters.eq("login", login))
                .first();
    }

}