package org.nbd.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.exceptions.LoginAlreadyExists;
import org.nbd.exceptions.UserNotFoundException;
import org.nbd.model.*;
import org.nbd.repositories.UserRepo;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@ApplicationScoped
public class UserService {

    @Inject
    private UserRepo userRepo;

    public User getUser(ObjectId id) {
        User user = userRepo.findById(id);
        if (user == null) throw new UserNotFoundException(id);
        return user;
    }

    public <T extends User> T createUser(T user) {
        try {
            return (T) userRepo.save(user);
        } catch (Exception e) {
            throw new LoginAlreadyExists(user.getLogin());
        }
    }

    public User getByLogin(String login) {
        User user = userRepo.findByLogin(login);
        if (user == null) throw new UserNotFoundException(login);
        return user;
    }

    public List<User> searchByLogin(String partial) {
        return userRepo.findAllByLoginContainingIgnoreCase(partial);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public <T extends User> T updateUser(ObjectId id, T updated) {
        User user = getUser(id);

        user.setLogin(updated.getLogin());
        user.setFirstName(updated.getFirstName());
        user.setLastName(updated.getLastName());
        user.setPhoneNumber(updated.getPhoneNumber());

        return (T) userRepo.save(user);
    }

    public User activate(ObjectId id) {
        User user = getUser(id);
        user.setActive(true);
        return userRepo.save(user);
    }

    public User deactivate(ObjectId id) {
        User user = getUser(id);
        user.setActive(false);
        return userRepo.save(user);
    }



    public Client getClient(ObjectId id) {
        User user = getUser(id);
        if (user instanceof Client c) return c;
        throw new UserNotFoundException(id);
    }

    public Client getClientByLogin(String login) {
        User user = getByLogin(login);
        if (user instanceof Client c) return c;
        throw new UserNotFoundException(login);
    }

    public List<Client> searchClients(String partial) {
        return searchByLogin(partial).stream()
                .filter(u -> u instanceof Client)
                .map(u -> (Client) u)
                .collect(Collectors.toList());
    }

    public List<Client> getAllClients() {
        return userRepo.findClients();
    }

    public Client updateClient(ObjectId id, Client updated) {
        return updateUser(id, updated);
    }

    public Employee getEmployee(ObjectId id) {
        User user = getUser(id);
        if (user instanceof Employee e) return e;
        throw new UserNotFoundException(id);
    }

    public Employee getEmployeeByLogin(String login) {
        User user = getByLogin(login);
        if (user instanceof Employee e) return e;
        throw new UserNotFoundException(login);
    }

    public List<Employee> searchEmployees(String partial) {
        return userRepo.findEmployees();
    }

    public List<Employee> getAllEmployees() {
        return getAllUsers().stream()
                .filter(u -> u instanceof Employee)
                .map(u -> (Employee) u)
                .collect(Collectors.toList());
    }

    public Employee updateEmployee(ObjectId id, Employee updated) {
        return updateUser(id, updated);
    }

    public Administrator getAdministrator(ObjectId id) {
        User user = getUser(id);
        if (user instanceof Administrator a) return a;
        throw new UserNotFoundException(id);
    }

    public Administrator getAdministratorByLogin(String login) {
        User user = getByLogin(login);
        if (user instanceof Administrator a) return a;
        throw new UserNotFoundException(login);
    }

    public List<Administrator> searchAdministrators(String partial) {
        return searchByLogin(partial).stream()
                .filter(u -> u instanceof Administrator)
                .map(u -> (Administrator) u)
                .collect(Collectors.toList());
    }

    public List<Administrator> getAllAdministrators() {
        return userRepo.findAdministrators();
    }

    public Administrator updateAdministrator(ObjectId id, Administrator updated) {
        return updateUser(id, updated);
    }
}
