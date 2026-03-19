package org.nbd.ports.input.employees;

import org.bson.types.ObjectId;
import org.nbd.model.Employee;

import java.util.List;

public interface EmployeeQueryUseCase {
    Employee getEmployee(ObjectId id);
    Employee getEmployeeByLogin(String login);
    List<Employee> searchEmployees(String partial);
    List<Employee> getAllEmployees();
}
