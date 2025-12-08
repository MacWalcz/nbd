package org.nbd.repositories;

import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.nbd.model.*;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserRepo extends BaseRepo<User> implements RepoManager<User> {

    // Конструктор по умолчанию (public), необходимый для проксирования CDI
    public UserRepo() {
        super();
    }

    @Inject
    public UserRepo(MongoDatabase database) {
        super(database);
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

    @Override
    public void update(ObjectId id, User updated) {
        collection.replaceOne(Filters.eq("_id", id), updated);
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
        return collection.find().into(new ArrayList<>());
    }

    public User findByLogin(String login) {
        return collection.find(Filters.eq("login", login)).first();
    }

    public boolean existsByLogin(String login) {
        return collection.find(Filters.eq("login", login)).first() != null;
    }

    public List<User> findByLoginPartial(String partial) {
        return collection
                .find(Filters.regex("login", partial, "i"))
                .into(new ArrayList<>());
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

        for (User u : collection.find()) {
            if (u.getLogin() != null && u.getLogin().toLowerCase().contains(lowerPartial)) {
                result.add(u);
            }
        }

        return result;
    }
}