package org.nbd.ports.output.employees;

import org.bson.types.ObjectId;
import org.nbd.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeQueryPort {
    Optional<Employee> findById(ObjectId id);
    Optional<Employee> findByLogin(String login);
    List<Employee> findAll();
    List<Employee> findAllByLoginContainingIgnoreCase(String partial);
}
