package org.nbd.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.converters.AdministratorConverter;
import org.nbd.converters.ClientConverter;
import org.nbd.converters.EmployeeConverter;
import org.nbd.exceptions.LoginAlreadyExists;
import org.nbd.exceptions.UserNotFoundException;
import org.nbd.model.*;
import org.nbd.repositories.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@ApplicationScoped
public class UserService {

    @Inject
    private UserRepo userRepo;

    //CLIENTS
    public Client createClient(Client client) {
        try {
            return userRepo.saveClient(client);
        } catch (Exception e) {
            throw new LoginAlreadyExists(client.getLogin());
        }
    }

    public Client getClient(ObjectId id) {
        Client client = userRepo.findClientById(id);
        if (client == null) throw new UserNotFoundException(id);
        return client;
    }

    public Client getClientByLogin(String login) {
        Client client = userRepo.findClientByLogin(login);
        if (client == null) throw new UserNotFoundException(login);
        return client;
    }

    public List<Client> searchClients(String partial) {
        return userRepo.findClientsByLoginPartial(partial);
    }

    public List<Client> getAllClients() {
        return userRepo.findAllClients();
    }

    public Client updateClient(ObjectId id, Client updated) {
        Client client = getClient(id);
        client.setLogin(updated.getLogin());
        client.setFirstName(updated.getFirstName());
        client.setLastName(updated.getLastName());
        client.setPhoneNumber(updated.getPhoneNumber());
        client.setActive(updated.isActive());
        return userRepo.updateClient(id, client);
    }

    public Client activateClient(ObjectId id) {
        Client client = getClient(id);
        client.setActive(true);
        return userRepo.updateClient(id, client);
    }

    public Client deactivateClient(ObjectId id) {
        Client client = getClient(id);
        client.setActive(false);
        return userRepo.updateClient(id, client);
    }

    //EMPLOYEES
    public Employee getEmployee(ObjectId id) {
        Employee e = userRepo.findEmployeeById(id);
        if (e == null) throw new UserNotFoundException(id);
        return e;
    }

    public Employee createEmployee(Employee employee) {
        try {
            return userRepo.saveEmployee(employee);
        } catch (Exception e) {
            throw new LoginAlreadyExists(employee.getLogin());
        }
    }

    public Employee getEmployeeByLogin(String login) {
        Employee e = userRepo.findEmployeeByLogin(login);
        if (e == null) throw new UserNotFoundException(login);
        return e;
    }

    public List<Employee> searchEmployees(String partial) {
        return userRepo.findEmployeesByLoginPartial(partial);
    }

    public List<Employee> getAllEmployees() {
        return userRepo.findAllEmployees();
    }

    public Employee updateEmployee(ObjectId id, Employee updated) {
        Employee e = getEmployee(id);
        e.setLogin(updated.getLogin());
        e.setFirstName(updated.getFirstName());
        e.setLastName(updated.getLastName());
        e.setPhoneNumber(updated.getPhoneNumber());
        e.setActive(updated.isActive());
        return userRepo.updateEmployee(id, e);
    }

    public Employee activateEmployee(ObjectId id) {
        Employee e = getEmployee(id);
        e.setActive(true);
        return userRepo.updateEmployee(id, e);
    }

    public Employee deactivateEmployee(ObjectId id) {
        Employee e = getEmployee(id);
        e.setActive(false);
        return userRepo.updateEmployee(id, e);
    }

    //ADMINISTRATORS
    public Administrator createAdministrator(Administrator admin) {
        try {
            return userRepo.saveAdministrator(admin);
        } catch (Exception e) {
            throw new LoginAlreadyExists(admin.getLogin());
        }
    }

    public Administrator getAdministrator(ObjectId id) {
        Administrator a = userRepo.findAdministratorById(id);
        if (a == null) throw new UserNotFoundException(id);
        return a;
    }

    public Administrator getAdministratorByLogin(String login) {
        Administrator a = userRepo.findAdministratorByLogin(login);
        if (a == null) throw new UserNotFoundException(login);
        return a;
    }

    public List<Administrator> searchAdministrators(String partial) {
        return userRepo.findAdministratorsByLoginPartial(partial);
    }

    public List<Administrator> getAllAdministrators() {
        return userRepo.findAllAdministrators();
    }

    public Administrator updateAdministrator(ObjectId id, Administrator updated) {
        Administrator a = getAdministrator(id);
        a.setLogin(updated.getLogin());
        a.setFirstName(updated.getFirstName());
        a.setLastName(updated.getLastName());
        a.setPhoneNumber(updated.getPhoneNumber());
        a.setActive(updated.isActive());
        return userRepo.updateAdministrator(id, a);
    }

    public Administrator activateAdministrator(ObjectId id) {
        Administrator e = getAdministrator(id);
        e.setActive(true);
        return userRepo.updateAdministrator(id, e);
    }

    public Administrator deactivateAdministrator(ObjectId id) {
        Administrator e = getAdministrator(id);
        e.setActive(false);
        return userRepo.updateAdministrator(id, e);
    }

    public List<Object> getAllUsersMixed() {
        List<Object> all = new ArrayList<>();
        all.addAll(userRepo.findAllClients().stream()
                .map(ClientConverter::clientToClientDTO)
                .toList());
        all.addAll(userRepo.findAllEmployees().stream()
                .map(EmployeeConverter::employeeToEmployeeDTO)
                .toList());
        all.addAll(userRepo.findAllAdministrators().stream()
                .map(AdministratorConverter::administratorToAdministratorDTO)
                .toList());
        return all;
    }

}
