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

    public User activateClient(ObjectId id) {
        Client user = userRepo.findClientById(id);

        if (user == null) throw new UserNotFoundException(id);

        Client newClient = new Client();

        newClient.setId(user.getId());
        newClient.setLogin(user.getLogin());
        newClient.setFirstName(user.getFirstName());
        newClient.setLastName(user.getLastName());
        newClient.setPhoneNumber(user.getPhoneNumber());
        newClient.setActive(true);

        return userRepo.updateClient(id, newClient);
    }

    public User deactivateClient(ObjectId id) {
        Client user = userRepo.findClientById(id);

        if (user == null) throw new UserNotFoundException(id);

        Client newClient = new Client();

        newClient.setId(user.getId());
        newClient.setLogin(user.getLogin());
        newClient.setFirstName(user.getFirstName());
        newClient.setLastName(user.getLastName());
        newClient.setPhoneNumber(user.getPhoneNumber());
        newClient.setActive(false);

        return userRepo.updateClient(id, newClient);
    }


    public Client getClient(ObjectId id) {

        Client user = userRepo.findClientById(id);
        if (user == null) throw new UserNotFoundException(id);
        return user;

    }

    public Client getClientByLogin(String partial) {
        return userRepo.findClientByLogin(partial);
    }

    public List<Client> searchClients(String partial) {
        return userRepo.findAllClientsByLoginContainingIgnoreCase(partial);
    }

    public List<Client> getAllClients() {
        return userRepo.findClients();
    }

    public Client updateClient(ObjectId id, Client updated) {
        Client user = userRepo.findClientById(id);

        if (user == null) throw new UserNotFoundException(id);

        Client newClient = new Client();

        newClient.setId(user.getId());
        newClient.setLogin(updated.getLogin());
        newClient.setFirstName(updated.getFirstName());
        newClient.setLastName(updated.getLastName());
        newClient.setPhoneNumber(updated.getPhoneNumber());
        newClient.setActive(updated.isActive());

        return userRepo.updateClient(id, newClient);
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
