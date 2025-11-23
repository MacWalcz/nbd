package org.nbd.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.nbd.exceptions.LoginAlreadyExists;
import org.nbd.exceptions.UserNotFoundException;
import org.nbd.model.Client;
import org.nbd.model.Employee;
import org.nbd.repositories.EmployeeRepo;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Component
@Service
public class EmployeeService {

    private final @NonNull EmployeeRepo employeeRepo;

    public Employee getEmployee(ObjectId id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public Employee createEmployee(Employee employee) {
        try {
            return employeeRepo.save(employee);
        } catch (DuplicateKeyException e) {
            throw new LoginAlreadyExists(employee.getLogin());
        }
    }

    public Employee getByLogin(String login) {
        return employeeRepo.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    public List<Employee> searchByLogin(String partial) {
        return employeeRepo.findAllByLoginContainingIgnoreCase(partial);
    }

    public List<Employee> getAll() {
        return employeeRepo.findAll();
    }

    public Employee update(ObjectId id, Employee updatedEmployee) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        employee.setLogin(updatedEmployee.getLogin());
        if (updatedEmployee.getFirstName() != null) employee.setFirstName(updatedEmployee.getFirstName());
        if (updatedEmployee.getLastName() != null) employee.setLastName(updatedEmployee.getLastName());
        if (updatedEmployee.getPhoneNumber() != null) employee.setPhoneNumber(updatedEmployee.getPhoneNumber());

        return employeeRepo.save(employee);
    }

    public Employee activate(ObjectId id) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        employee.setActive(true);
        return employeeRepo.save(employee);
    }

    public Employee deactivate(ObjectId id) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        employee.setActive(false);
        return employeeRepo.save(employee);
    }
}
