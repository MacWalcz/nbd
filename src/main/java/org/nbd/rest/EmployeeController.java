package org.nbd.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nbd.converters.EmployeeConverter;
import org.nbd.dto.AdministratorDTO;
import org.nbd.dto.ClientDTO;
import org.nbd.dto.EmployeeDTO;
import org.nbd.model.Administrator;
import org.nbd.model.Client;
import org.nbd.model.Employee;
import org.nbd.services.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employees")
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
public class EmployeeController {

    private final EmployeeService service;
    private final EmployeeConverter converter;

    @GetMapping("/{id}")
    public EmployeeDTO getEmployee(@PathVariable String id) {
        return converter.employeeToEmployeeDTO(service.getEmployee(id));
    }

    @PostMapping
    public EmployeeDTO postEmployee(@Valid @RequestBody EmployeeDTO dto) {
        Employee employee = converter.employeeDTOToEmployee(dto);
        Employee saved = service.createEmployee(employee);
        return converter.employeeToEmployeeDTO(saved);
    }

    @GetMapping("/by-login/{login}")
    public Employee getExact(@PathVariable String login) {
        return service.getByLogin(login);
    }

    @GetMapping("/search")
    public List<Employee> search(@RequestParam String q) {
        return service.searchByLogin(q);
    }

    @GetMapping
    public List<Employee> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable String id, @Valid @RequestBody EmployeeDTO dto) {
        Employee employee = converter.employeeDTOToEmployee(dto);
        return service.update(id, employee);
    }

    @PatchMapping("/{id}/activate")
    public Employee activate(@PathVariable String id) {
        return service.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    public Employee deactivate(@PathVariable String id) {
        return service.deactivate(id);
    }
}