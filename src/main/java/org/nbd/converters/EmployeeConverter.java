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
                employee.isActive()
        );
    }

    public static Employee employeeDTOToEmployee(EmployeeDTO dto) {
        return Employee.builder()
                .id(dto.getId())
                .login(dto.getLogin())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phoneNumber(dto.getPhoneNumber())
                .active(dto.getActive())
                .build();
    }
}
