package org.nbd.repositories;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.nbd.model.*;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserRepo {

    protected MongoDatabase database;

    public UserRepo() {}

    @Inject
    public UserRepo(MongoDatabase database) {
        this.database = database;
    }

    @PostConstruct
    public void init() {
        try {
            database.getCollection("users").createIndex(
                    Indexes.ascending("login"),
                    new IndexOptions().unique(true)
            );
            System.out.println("Utworzono unikalny indeks dla pola 'login'.");
        } catch (Exception e) {
        }
    }

    //CLIENT
    public Client saveClient(Client client) {
        database.getCollection("users", Client.class).insertOne(client);
        return client;
    }

    public Client findClientById(ObjectId id) {
        return database.getCollection("users", Client.class)
                .find(Filters.eq("_id", id))
                .first();
    }

    public Client findClientByLogin(String login) {
        return database.getCollection("users", Client.class)
                .find(Filters.eq("login", login))
                .first();
    }

    public List<Client> findAllClients() {
        return database.getCollection("users", Client.class)
                .find(Filters.eq("_class", "client"))
                .into(new ArrayList<>());
    }

    public List<Client> findClientsByLoginPartial(String partial) {
        List<Client> result = new ArrayList<>();
        String lowerPartial = partial.toLowerCase();

        for (Client c : findAllClients()) {
            if (c.getLogin() != null && c.getLogin().toLowerCase().contains(lowerPartial)) {
                result.add(c);
            }
        }
        return result;
    }

    public Client updateClient(ObjectId id, Client updated) {
        updated.setId(id);
        database.getCollection("users", Client.class)
                .replaceOne(Filters.eq("_id", id), updated);
        return updated;
    }

    //EMPLOYEE
    public Employee saveEmployee(Employee employee) {
        database.getCollection("users", Employee.class).insertOne(employee);
        return employee;
    }

    public Employee findEmployeeById(ObjectId id) {
        return database.getCollection("users", Employee.class)
                .find(Filters.eq("_id", id))
                .first();
    }

    public Employee findEmployeeByLogin(String login) {
        return database.getCollection("users", Employee.class)
                .find(Filters.eq("login", login))
                .first();
    }

    public List<Employee> findAllEmployees() {
        return database.getCollection("users", Employee.class)
                .find(Filters.eq("_class", "employee"))
                .into(new ArrayList<>());
    }

    public Employee updateEmployee(ObjectId id, Employee updated) {
        updated.setId(id);
        database.getCollection("users", Employee.class)
                .replaceOne(Filters.eq("_id", id), updated);
        return updated;
    }

    public List<Employee> findEmployeesByLoginPartial(String partial) {
        List<Employee> result = new ArrayList<>();
        String lowerPartial = partial.toLowerCase();

        for (Employee e : findAllEmployees()) {
            if (e.getLogin() != null && e.getLogin().toLowerCase().contains(lowerPartial)) {
                result.add(e);
            }
        }
        return result;
    }

    //ADMINISTRATOR
    public Administrator saveAdministrator(Administrator admin) {
        database.getCollection("users", Administrator.class).insertOne(admin);
        return admin;
    }

    public Administrator findAdministratorById(ObjectId id) {
        return database.getCollection("users", Administrator.class)
                .find(Filters.eq("_id", id))
                .first();
    }

    public Administrator findAdministratorByLogin(String login) {
        return database.getCollection("users", Administrator.class)
                .find(Filters.eq("login", login))
                .first();
    }

    public List<Administrator> findAllAdministrators() {
        return database.getCollection("users", Administrator.class)
                .find(Filters.eq("_class", "administrator"))
                .into(new ArrayList<>());
    }

    public Administrator updateAdministrator(ObjectId id, Administrator updated) {
        updated.setId(id);
        database.getCollection("users", Administrator.class)
                .replaceOne(Filters.eq("_id", id), updated);
        return updated;
    }

    public List<Administrator> findAdministratorsByLoginPartial(String partial) {
        List<Administrator> result = new ArrayList<>();
        String lowerPartial = partial.toLowerCase();

        for (Administrator a : findAllAdministrators()) {
            if (a.getLogin() != null && a.getLogin().toLowerCase().contains(lowerPartial)) {
                result.add(a);
            }
        }
        return result;
    }
}
