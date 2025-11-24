package org.nbd.services;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.exceptions.LoginAlreadyExists;
import org.nbd.exceptions.UserNotFoundException;
import org.nbd.model.*;
import org.nbd.repositories.UserRepo;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;

    public User getUser(ObjectId id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public <T extends User> T createUser(T user) {
        try {
            return (T) userRepo.save(user);
        } catch (DuplicateKeyException e) {
            throw new LoginAlreadyExists(user.getLogin());
        }
    }

    public User getByLogin(String login) {
        return userRepo.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
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

        if (user instanceof Client && updated instanceof Client c) {
            ((Client) user).setClientType(c.getClientType());
        }

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
        return getAllUsers().stream()
                .filter(u -> u instanceof Client)
                .map(u -> (Client) u)
                .collect(Collectors.toList());
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
        return searchByLogin(partial).stream()
                .filter(u -> u instanceof Employee)
                .map(u -> (Employee) u)
                .collect(Collectors.toList());
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
        return getAllUsers().stream()
                .filter(u -> u instanceof Administrator)
                .map(u -> (Administrator) u)
                .collect(Collectors.toList());
    }

    public Administrator updateAdministrator(ObjectId id, Administrator updated) {
        return updateUser(id, updated);
    }
}
