package org.nbd.services;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.exceptions.UserNotFoundException;
import org.nbd.model.Employee;
import org.nbd.ports.input.employees.EmployeeQueryUseCase;
import org.nbd.ports.output.employees.EmployeeQueryPort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class EmployeeService implements EmployeeQueryUseCase {

    private EmployeeQueryPort employeeQueryPort;

    @Override
    public Employee getEmployee(ObjectId id) {
        return employeeQueryPort.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public Employee getEmployeeByLogin(String login) {
        return employeeQueryPort.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
    }

    @Override
    public List<Employee> searchEmployees(String partial) {
        return employeeQueryPort.findAllByLoginContainingIgnoreCase(partial);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeQueryPort.findAll();
    }

}
