package org.nbd.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.converters.EmployeeConverter;
import org.nbd.dto.EmployeeDTO;
import org.nbd.model.Employee;
import org.nbd.services.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.nbd.converters.EmployeeConverter.employeeDTOToEmployee;
import static org.nbd.converters.EmployeeConverter.employeeToEmployeeDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employees")
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping("/{id}")
    public EmployeeDTO getEmployee(@PathVariable String id) {
        return employeeToEmployeeDTO(service.getEmployee(new ObjectId(id)));
    }

    @PostMapping
    public EmployeeDTO postEmployee(@Valid @RequestBody EmployeeDTO dto) {
        Employee saved = service.createEmployee(employeeDTOToEmployee(dto));
        return employeeToEmployeeDTO(saved);
    }

    @GetMapping("/by-login/{login}")
    public EmployeeDTO getExact(@PathVariable String login) {
        return employeeToEmployeeDTO(service.getByLogin(login));
    }

    @GetMapping("/search")
    public List<EmployeeDTO> search(@RequestParam String q) {
        return service.searchByLogin(q)
                .stream()
                .map(EmployeeConverter::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<EmployeeDTO> getAll() {
        return service.getAll()
                .stream()
                .map(EmployeeConverter::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public EmployeeDTO update(@PathVariable String id, @Valid @RequestBody EmployeeDTO dto) {
        Employee updated = service.update(new ObjectId(id), employeeDTOToEmployee(dto));
        return employeeToEmployeeDTO(updated);
    }

    @PatchMapping("/{id}/activate")
    public EmployeeDTO activate(@PathVariable String id) {
        return employeeToEmployeeDTO(service.activate(new ObjectId(id)));
    }

    @PatchMapping("/{id}/deactivate")
    public EmployeeDTO deactivate(@PathVariable String id) {
        return employeeToEmployeeDTO(service.deactivate(new ObjectId(id)));
    }
}
