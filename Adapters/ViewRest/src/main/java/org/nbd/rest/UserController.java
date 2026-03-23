package org.nbd.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.converters.*;
import org.nbd.dto.*;
import org.nbd.model.*;
import org.nbd.security.SignatureManager;
import org.nbd.services.AdministratorService;
import org.nbd.services.ClientService;
import org.nbd.services.EmployeeService;
import org.nbd.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ClientService clientService;
    private final AdministratorService administratorService;
    private final EmployeeService employeeService;

    private final SignatureManager signatureManager;

    @GetMapping("/clients/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public ResponseEntity<ClientDTO> getClient(@PathVariable String id) {
        ClientDTO dto = ClientConverter.clientToClientDTO(clientService.getClient(new ObjectId(id)));
        return ResponseEntity.ok()
                .header("ETag", signatureManager.sign(id))
                .body(dto);
    }

    @PostMapping("/clients")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ClientDTO createClient(@Valid @RequestBody ClientDTO dto) {
        Client saved = userService.createUser(ClientConverter.clientDTOToClient(dto));
        return ClientConverter.clientToClientDTO(saved);
    }

    @GetMapping("/clients/by-login/{login}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public ClientDTO getClientByLogin(@PathVariable String login) {
        return ClientConverter.clientToClientDTO(clientService.getClientByLogin(login));
    }

    @GetMapping("/clients/search")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public List<ClientDTO> searchClients(@RequestParam String q) {
        return clientService.searchClients(q)
                .stream()
                .map(ClientConverter::clientToClientDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/clients")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients()
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
        Client updated = userService.updateUser(new ObjectId(id), ClientConverter.clientDTOToClient(dto));
        return ClientConverter.clientToClientDTO(updated);
    }

    @PatchMapping("/clients/{id}/activate")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ClientDTO activateClient(@PathVariable String id) {
        return ClientConverter.clientToClientDTO((Client) userService.activate(new ObjectId(id)));
    }

    @PatchMapping("/clients/{id}/deactivate")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ClientDTO deactivateClient(@PathVariable String id) {
        return ClientConverter.clientToClientDTO((Client) userService.deactivate(new ObjectId(id)));
    }

    @GetMapping("/employees/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable String id) {
        EmployeeDTO dto = EmployeeConverter.employeeToEmployeeDTO(employeeService.getEmployee(new ObjectId(id)));
        return ResponseEntity.ok()
                .header("ETag", signatureManager.sign(id))
                .body(dto);
    }

    @PostMapping("/employees")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public EmployeeDTO createEmployee(@Valid @RequestBody EmployeeDTO dto) {
        Employee saved = userService.createUser(EmployeeConverter.employeeDTOToEmployee(dto));
        return EmployeeConverter.employeeToEmployeeDTO(saved);
    }

    @GetMapping("/employees/by-login/{login}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public EmployeeDTO getEmployeeByLogin(@PathVariable String login) {
        return EmployeeConverter.employeeToEmployeeDTO(employeeService.getEmployeeByLogin(login));
    }

    @GetMapping("/employees/search")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<EmployeeDTO> searchEmployees(@RequestParam String q) {
        return employeeService.searchEmployees(q)
                .stream()
                .map(EmployeeConverter::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/employees")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees()
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
        Employee updated = userService.updateUser(new ObjectId(id), EmployeeConverter.employeeDTOToEmployee(dto));
        return EmployeeConverter.employeeToEmployeeDTO(updated);
    }

    @PatchMapping("/employees/{id}/activate")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public EmployeeDTO activateEmployee(@PathVariable String id) {
        return EmployeeConverter.employeeToEmployeeDTO((Employee) userService.activate(new ObjectId(id)));
    }

    @PatchMapping("/employees/{id}/deactivate")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public EmployeeDTO deactivateEmployee(@PathVariable String id) {
        return EmployeeConverter.employeeToEmployeeDTO((Employee) userService.deactivate(new ObjectId(id)));
    }

    @GetMapping("/administrators/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<AdministratorDTO> getAdministrator(@PathVariable String id) {
        AdministratorDTO dto = AdministratorConverter.administratorToAdministratorDTO(administratorService.getAdministrator(new ObjectId(id)));
        return ResponseEntity.ok()
                .header("ETag", signatureManager.sign(id))
                .body(dto);
    }

    @PostMapping("/administrators")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public AdministratorDTO createAdministrator(@Valid @RequestBody AdministratorDTO dto) {
        Administrator saved = userService.createUser(AdministratorConverter.administratorDTOToAdministrator(dto));
        return AdministratorConverter.administratorToAdministratorDTO(saved);
    }

    @GetMapping("/administrators/by-login/{login}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public AdministratorDTO getAdministratorByLogin(@PathVariable String login) {
        return AdministratorConverter.administratorToAdministratorDTO(administratorService.getAdministratorByLogin(login));
    }

    @GetMapping("/administrators/search")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<AdministratorDTO> searchAdministrators(@RequestParam String q) {
        return administratorService.searchAdministrators(q)
                .stream()
                .map(AdministratorConverter::administratorToAdministratorDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/administrators")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<AdministratorDTO> getAllAdministrators() {
        return administratorService.getAllAdministrators()
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
        Administrator updated = userService.updateUser(new ObjectId(id), AdministratorConverter.administratorDTOToAdministrator(dto));
        return AdministratorConverter.administratorToAdministratorDTO(updated);
    }

    @PatchMapping("/administrators/{id}/activate")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public AdministratorDTO activateAdministrator(@PathVariable String id) {
        return AdministratorConverter.administratorToAdministratorDTO((Administrator) userService.activate(new ObjectId(id)));
    }

    @PatchMapping("/administrators/{id}/deactivate")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public AdministratorDTO deactivateAdministrator(@PathVariable String id) {
        return AdministratorConverter.administratorToAdministratorDTO((Administrator) userService.deactivate(new ObjectId(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public List<Object> getAllUsers() {
        return userService.getAllUsers()
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
        userService.changePassword(new ObjectId(id),req.password());
        return ResponseEntity.ok().build();
    }
}
