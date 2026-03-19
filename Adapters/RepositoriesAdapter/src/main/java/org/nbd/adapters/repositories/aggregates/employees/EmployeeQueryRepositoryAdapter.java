package org.nbd.adapters.repositories.aggregates.employees;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.adapters.repositories.EmployeeRepo;
import org.nbd.adapters.repositories.mappers.EmployeeEntMapper;
import org.nbd.model.Employee;
import org.nbd.ports.output.employees.EmployeeQueryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class EmployeeQueryRepositoryAdapter implements EmployeeQueryPort {

    private final EmployeeEntMapper employeeEntMapper;
    private final EmployeeRepo employeeRepo;


    @Override
    public Optional<Employee> findById(ObjectId id) {
        return employeeRepo.findById(id).map(employeeEntMapper::toEmployee);
    }

    @Override
    public Optional<Employee> findByLogin(String login) {
        return employeeRepo.findByLogin(login).map(employeeEntMapper::toEmployee);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepo.findAll().stream().map(employeeEntMapper::toEmployee).toList();
    }

    @Override
    public List<Employee> findAllByLoginContainingIgnoreCase(String partial) {
        return employeeRepo.findAllByLoginContainingIgnoreCase(partial).stream().map(employeeEntMapper::toEmployee).toList();
    }
}
