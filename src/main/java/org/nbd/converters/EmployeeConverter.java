package org.nbd.converters;

import lombok.RequiredArgsConstructor;
import org.nbd.dto.EmployeeDTO;
import org.nbd.model.Employee;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeConverter {

    public EmployeeDTO employeeToEmployeeDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getLogin(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhoneNumber(),
                employee.isActive()
        );
    }

    public Employee employeeDTOToEmployee(EmployeeDTO dto) {

        return Employee.builder()
                .id(dto.id())
                .login(dto.login())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .phoneNumber(dto.phoneNumber())
                .active(dto.active())
                .build();
    }
}

