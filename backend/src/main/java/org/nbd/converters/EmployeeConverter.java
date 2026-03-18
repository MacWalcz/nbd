package org.nbd.converters;

import org.nbd.dto.EmployeeDTO;
import org.nbd.model.Employee;

public class EmployeeConverter {

    public static EmployeeDTO employeeToEmployeeDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getLogin(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhoneNumber(),
                employee.isActive(),
                employee.getPosition(),
                employee.getPassword()
        );
    }

    public static Employee employeeDTOToEmployee(EmployeeDTO dto) {

        return Employee.builder()
                .id(dto.id())
                .login(dto.login())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .phoneNumber(dto.phoneNumber())
                .active(dto.active())
                .position(dto.position())
                .password(dto.password())
                .build();
    }
}

