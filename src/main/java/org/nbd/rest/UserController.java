package org.nbd.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.converters.*;
import org.nbd.dto.*;
import org.nbd.model.*;
import org.nbd.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
public class UserController {

    private final UserService service;

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable String id) {
        return ClientConverter.clientToClientDTO(service.getClient(new ObjectId(id)));
    }

    @PostMapping("/clients")
    public ClientDTO createClient(@Valid @RequestBody ClientDTO dto) {
        Client saved = service.createUser(ClientConverter.clientDTOToClient(dto));
        return ClientConverter.clientToClientDTO(saved);
    }

    @GetMapping("/clients/by-login/{login}")
    public ClientDTO getClientByLogin(@PathVariable String login) {
        return ClientConverter.clientToClientDTO(service.getClientByLogin(login));
    }

    @GetMapping("/clients/search")
    public List<ClientDTO> searchClients(@RequestParam String q) {
        return service.searchClients(q)
                .stream()
                .map(ClientConverter::clientToClientDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/clients")
    public List<ClientDTO> getAllClients() {
        return service.getAllClients()
                .stream()
                .map(ClientConverter::clientToClientDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/clients/{id}")
    public ClientDTO updateClient(@PathVariable String id, @Valid @RequestBody ClientDTO dto) {
        Client updated = service.updateClient(new ObjectId(id), ClientConverter.clientDTOToClient(dto));
        return ClientConverter.clientToClientDTO(updated);
    }

    @PatchMapping("/clients/{id}/activate")
    public ClientDTO activateClient(@PathVariable String id) {
        return ClientConverter.clientToClientDTO((Client) service.activate(new ObjectId(id)));
    }

    @PatchMapping("/clients/{id}/deactivate")
    public ClientDTO deactivateClient(@PathVariable String id) {
        return ClientConverter.clientToClientDTO((Client) service.deactivate(new ObjectId(id)));
    }

    @GetMapping("/employees/{id}")
    public EmployeeDTO getEmployee(@PathVariable String id) {
        return EmployeeConverter.employeeToEmployeeDTO(service.getEmployee(new ObjectId(id)));
    }

    @PostMapping("/employees")
    public EmployeeDTO createEmployee(@Valid @RequestBody EmployeeDTO dto) {
        Employee saved = service.createUser(EmployeeConverter.employeeDTOToEmployee(dto));
        return EmployeeConverter.employeeToEmployeeDTO(saved);
    }

    @GetMapping("/employees/by-login/{login}")
    public EmployeeDTO getEmployeeByLogin(@PathVariable String login) {
        return EmployeeConverter.employeeToEmployeeDTO(service.getEmployeeByLogin(login));
    }

    @GetMapping("/employees/search")
    public List<EmployeeDTO> searchEmployees(@RequestParam String q) {
        return service.searchEmployees(q)
                .stream()
                .map(EmployeeConverter::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/employees")
    public List<EmployeeDTO> getAllEmployees() {
        return service.getAllEmployees()
                .stream()
                .map(EmployeeConverter::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/employees/{id}")
    public EmployeeDTO updateEmployee(@PathVariable String id, @Valid @RequestBody EmployeeDTO dto) {
        Employee updated = service.updateEmployee(new ObjectId(id), EmployeeConverter.employeeDTOToEmployee(dto));
        return EmployeeConverter.employeeToEmployeeDTO(updated);
    }

    @PatchMapping("/employees/{id}/activate")
    public EmployeeDTO activateEmployee(@PathVariable String id) {
        return EmployeeConverter.employeeToEmployeeDTO((Employee) service.activate(new ObjectId(id)));
    }

    @PatchMapping("/employees/{id}/deactivate")
    public EmployeeDTO deactivateEmployee(@PathVariable String id) {
        return EmployeeConverter.employeeToEmployeeDTO((Employee) service.deactivate(new ObjectId(id)));
    }

    @GetMapping("/administrators/{id}")
    public AdministratorDTO getAdministrator(@PathVariable String id) {
        return AdministratorConverter.administratorToAdministratorDTO(service.getAdministrator(new ObjectId(id)));
    }

    @PostMapping("/administrators")
    public AdministratorDTO createAdministrator(@Valid @RequestBody AdministratorDTO dto) {
        Administrator saved = service.createUser(AdministratorConverter.administratorDTOToAdministrator(dto));
        return AdministratorConverter.administratorToAdministratorDTO(saved);
    }

    @GetMapping("/administrators/by-login/{login}")
    public AdministratorDTO getAdministratorByLogin(@PathVariable String login) {
        return AdministratorConverter.administratorToAdministratorDTO(service.getAdministratorByLogin(login));
    }

    @GetMapping("/administrators/search")
    public List<AdministratorDTO> searchAdministrators(@RequestParam String q) {
        return service.searchAdministrators(q)
                .stream()
                .map(AdministratorConverter::administratorToAdministratorDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/administrators")
    public List<AdministratorDTO> getAllAdministrators() {
        return service.getAllAdministrators()
                .stream()
                .map(AdministratorConverter::administratorToAdministratorDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/administrators/{id}")
    public AdministratorDTO updateAdministrator(@PathVariable String id, @Valid @RequestBody AdministratorDTO dto) {
        Administrator updated = service.updateAdministrator(new ObjectId(id), AdministratorConverter.administratorDTOToAdministrator(dto));
        return AdministratorConverter.administratorToAdministratorDTO(updated);
    }

    @PatchMapping("/administrators/{id}/activate")
    public AdministratorDTO activateAdministrator(@PathVariable String id) {
        return AdministratorConverter.administratorToAdministratorDTO((Administrator) service.activate(new ObjectId(id)));
    }

    @PatchMapping("/administrators/{id}/deactivate")
    public AdministratorDTO deactivateAdministrator(@PathVariable String id) {
        return AdministratorConverter.administratorToAdministratorDTO((Administrator) service.deactivate(new ObjectId(id)));
    }

    @GetMapping
    public List<Object> getAllUsers() {
        return service.getAllUsers()
                .stream()
                .map(user -> {
                    if (user instanceof Client c) {
                        return ClientConverter.clientToClientDTO(c);
                    } else if (user instanceof Employee e) {
                        return EmployeeConverter.employeeToEmployeeDTO(e);
                    } else if (user instanceof Administrator a) {
                        return AdministratorConverter.administratorToAdministratorDTO(a);
                    } else {
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }
}
