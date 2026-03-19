package org.nbd.adapters.repositories.mappers;

import org.mapstruct.Mapper;
import org.nbd.adapters.repositories.entities.EmployeeEnt;
import org.nbd.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeEntMapper {
    Employee toEmployee(EmployeeEnt employeeEnt);
    EmployeeEnt toEmployeeEnt(Employee employee);
}
