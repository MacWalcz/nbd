package org.nbd.rest;

import io.jsonwebtoken.Jwt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.converters.*;
import org.nbd.dto.*;
import org.nbd.model.*;
import org.nbd.security.SignatureManager;
import org.nbd.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final SignatureManager signatureManager;

    @GetMapping("/clients/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public ResponseEntity<ClientDTO> getClient(@PathVariable String id) {
        ClientDTO dto = ClientConverter.clientToClientDTO(service.getClient(new ObjectId(id)));
        return ResponseEntity.ok()
                .header("ETag", signatureManager.sign(id))
                .body(dto);
    }

    @PostMapping("/clients")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ClientDTO createClient(@Valid @RequestBody ClientDTO dto) {
        Client saved = service.createUser(ClientConverter.clientDTOToClient(dto));
        return ClientConverter.clientToClientDTO(saved);
    }

    @GetMapping("/clients/by-login/{login}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public ClientDTO getClientByLogin(@PathVariable String login) {
        return ClientConverter.clientToClientDTO(service.getClientByLogin(login));
    }

    @GetMapping("/clients/search")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public List<ClientDTO> searchClients(@RequestParam String q) {
        return service.searchClients(q)
                .stream()
                .map(ClientConverter::clientToClientDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/clients")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public List<ClientDTO> getAllClients() {
        return service.getAllClients()
                .stream()
                .map(ClientConverter::clientToClientDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/clients/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ClientDTO updateClient(@PathVariable String id, @Valid @RequestBody ClientDTO dto, @RequestHeader("If-Match") String signature) {
        if (!signatureManager.verify(signature.replace("\"", ""), id)) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Błędny podpis danych (JWS)!");
        }
        Client updated = service.updateClient(new ObjectId(id), ClientConverter.clientDTOToClient(dto));
        return ClientConverter.clientToClientDTO(updated);
    }

    @PatchMapping("/clients/{id}/activate")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ClientDTO activateClient(@PathVariable String id) {
        return ClientConverter.clientToClientDTO((Client) service.activate(new ObjectId(id)));
    }

    @PatchMapping("/clients/{id}/deactivate")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ClientDTO deactivateClient(@PathVariable String id) {
        return ClientConverter.clientToClientDTO((Client) service.deactivate(new ObjectId(id)));
    }

    @GetMapping("/employees/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable String id) {
        EmployeeDTO dto = EmployeeConverter.employeeToEmployeeDTO(service.getEmployee(new ObjectId(id)));
        return ResponseEntity.ok()
                .header("ETag", signatureManager.sign(id))
                .body(dto);
    }

    @PostMapping("/employees")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public EmployeeDTO createEmployee(@Valid @RequestBody EmployeeDTO dto) {
        Employee saved = service.createUser(EmployeeConverter.employeeDTOToEmployee(dto));
        return EmployeeConverter.employeeToEmployeeDTO(saved);
    }

    @GetMapping("/employees/by-login/{login}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public EmployeeDTO getEmployeeByLogin(@PathVariable String login) {
        return EmployeeConverter.employeeToEmployeeDTO(service.getEmployeeByLogin(login));
    }

    @GetMapping("/employees/search")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<EmployeeDTO> searchEmployees(@RequestParam String q) {
        return service.searchEmployees(q)
                .stream()
                .map(EmployeeConverter::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/employees")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<EmployeeDTO> getAllEmployees() {
        return service.getAllEmployees()
                .stream()
                .map(EmployeeConverter::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/employees/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public EmployeeDTO updateEmployee(@PathVariable String id, @Valid @RequestBody EmployeeDTO dto, @RequestHeader("If-Match") String signature) {
        if (!signatureManager.verify(signature.replace("\"", ""), id)) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Błędny podpis danych (JWS)!");
        }
        Employee updated = service.updateEmployee(new ObjectId(id), EmployeeConverter.employeeDTOToEmployee(dto));
        return EmployeeConverter.employeeToEmployeeDTO(updated);
    }

    @PatchMapping("/employees/{id}/activate")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public EmployeeDTO activateEmployee(@PathVariable String id) {
        return EmployeeConverter.employeeToEmployeeDTO((Employee) service.activate(new ObjectId(id)));
    }

    @PatchMapping("/employees/{id}/deactivate")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public EmployeeDTO deactivateEmployee(@PathVariable String id) {
        return EmployeeConverter.employeeToEmployeeDTO((Employee) service.deactivate(new ObjectId(id)));
    }

    @GetMapping("/administrators/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<AdministratorDTO> getAdministrator(@PathVariable String id) {
        AdministratorDTO dto = AdministratorConverter.administratorToAdministratorDTO(service.getAdministrator(new ObjectId(id)));
        return ResponseEntity.ok()
                .header("ETag", signatureManager.sign(id))
                .body(dto);
    }

    @PostMapping("/administrators")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public AdministratorDTO createAdministrator(@Valid @RequestBody AdministratorDTO dto) {
        Administrator saved = service.createUser(AdministratorConverter.administratorDTOToAdministrator(dto));
        return AdministratorConverter.administratorToAdministratorDTO(saved);
    }

    @GetMapping("/administrators/by-login/{login}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public AdministratorDTO getAdministratorByLogin(@PathVariable String login) {
        return AdministratorConverter.administratorToAdministratorDTO(service.getAdministratorByLogin(login));
    }

    @GetMapping("/administrators/search")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<AdministratorDTO> searchAdministrators(@RequestParam String q) {
        return service.searchAdministrators(q)
                .stream()
                .map(AdministratorConverter::administratorToAdministratorDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/administrators")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<AdministratorDTO> getAllAdministrators() {
        return service.getAllAdministrators()
                .stream()
                .map(AdministratorConverter::administratorToAdministratorDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/administrators/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public AdministratorDTO updateAdministrator(@PathVariable String id, @Valid @RequestBody AdministratorDTO dto, @RequestHeader("If-Match") String signature) {
        System.out.println(signature.replace("\"", ""));
        if (!signatureManager.verify(signature.replace("\"", ""), id)) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Błędny podpis danych (JWS)!");
        }
        Administrator updated = service.updateAdministrator(new ObjectId(id), AdministratorConverter.administratorDTOToAdministrator(dto));
        return AdministratorConverter.administratorToAdministratorDTO(updated);
    }

    @PatchMapping("/administrators/{id}/activate")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public AdministratorDTO activateAdministrator(@PathVariable String id) {
        return AdministratorConverter.administratorToAdministratorDTO((Administrator) service.activate(new ObjectId(id)));
    }

    @PatchMapping("/administrators/{id}/deactivate")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public AdministratorDTO deactivateAdministrator(@PathVariable String id) {
        return AdministratorConverter.administratorToAdministratorDTO((Administrator) service.deactivate(new ObjectId(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
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


    @PreAuthorize("#id == authentication.token.claims['id']")
    @PostMapping("/change-password/{id}")
    public ResponseEntity<?> resetPassword(@PathVariable String id,  @RequestBody ChangePasswordRequest req) {
        System.out.println(req);
        service.changePassword(new ObjectId(id),req.password());
        return ResponseEntity.ok().build();
    }
}
