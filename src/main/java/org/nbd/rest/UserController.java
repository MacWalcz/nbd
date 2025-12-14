package org.nbd.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.nbd.converters.*;
import org.nbd.dto.*;
import org.nbd.model.*;
import org.nbd.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserService service;


    @GET
    @Path("/clients/{id}")
    public ClientDTO getClient(@PathParam("id") String id) {
        return ClientConverter.clientToClientDTO(service.getClient(new ObjectId(id) ));
    }

    @POST
    @Path("/clients")
    public ClientDTO createClient(@Valid ClientDTO dto) {
        Client saved = service.createUser(ClientConverter.clientDTOToClient(dto));
        return ClientConverter.clientToClientDTO(saved);
    }

    @GET
    @Path("/clients/by-login/{login}")
    public ClientDTO getClientByLogin(@PathParam("login") String login) {
        return ClientConverter.clientToClientDTO(service.getClientByLogin(login));
    }

    @GET
    @Path("/clients/search")
    public List<ClientDTO> searchClients(@QueryParam("q") String q) {
        return service.searchClients(q)
                .stream()
                .map(ClientConverter::clientToClientDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/clients")
    public List<ClientDTO> getAllClients() {
        return service.getAllClients()
                .stream()
                .map(ClientConverter::clientToClientDTO)
                .collect(Collectors.toList());
    }

    @PUT
    @Path("/clients/{id}")
    public ClientDTO updateClient(@PathParam("id") String id, @Valid ClientDTO dto) {
        Client updated = service.updateClient(new ObjectId(id), ClientConverter.clientDTOToClient(dto));
        return ClientConverter.clientToClientDTO(updated);
    }

    @PATCH
    @Path("/clients/{id}/activate")
    public ClientDTO activateClient(@PathParam("id") String id) {
        return ClientConverter.clientToClientDTO((Client) service.activateClient(new ObjectId(id)));
    }

    @PATCH
    @Path("/clients/{id}/deactivate")
    public ClientDTO deactivateClient(@PathParam("id") String id) {
        return ClientConverter.clientToClientDTO((Client) service.deactivateClient(new ObjectId(id)));
    }


    @GET
    @Path("/employees/{id}")
    public EmployeeDTO getEmployee(@PathParam("id") String id) {
        return EmployeeConverter.employeeToEmployeeDTO(service.getEmployee(new ObjectId(id)));
    }

    @POST
    @Path("/employees")
    public EmployeeDTO createEmployee(@Valid EmployeeDTO dto) {
        Employee saved = service.createUser(EmployeeConverter.employeeDTOToEmployee(dto));
        return EmployeeConverter.employeeToEmployeeDTO(saved);
    }

    @GET
    @Path("/employees/by-login/{login}")
    public EmployeeDTO getEmployeeByLogin(@PathParam("login") String login) {
        return EmployeeConverter.employeeToEmployeeDTO(service.getEmployeeByLogin(login));
    }

    @GET
    @Path("/employees/search")
    public List<EmployeeDTO> searchEmployees(@QueryParam("q") String q) {
        return service.searchEmployees(q)
                .stream()
                .map(EmployeeConverter::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/employees")
    public List<EmployeeDTO> getAllEmployees() {
        return service.getAllEmployees()
                .stream()
                .map(EmployeeConverter::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @PUT
    @Path("/employees/{id}")
    public EmployeeDTO updateEmployee(@PathParam("id") String id, @Valid EmployeeDTO dto) {
        Employee updated = service.updateEmployee(new ObjectId(id), EmployeeConverter.employeeDTOToEmployee(dto));
        return EmployeeConverter.employeeToEmployeeDTO(updated);
    }

    @PATCH
    @Path("/employees/{id}/activate")
    public EmployeeDTO activateEmployee(@PathParam("id") String id) {
        return EmployeeConverter.employeeToEmployeeDTO((Employee) service.activate(new ObjectId(id)));
    }

    @PATCH
    @Path("/employees/{id}/deactivate")
    public EmployeeDTO deactivateEmployee(@PathParam("id") String id) {
        return EmployeeConverter.employeeToEmployeeDTO((Employee) service.deactivate(new ObjectId(id)));
    }


    @GET
    @Path("/administrators/{id}")
    public AdministratorDTO getAdministrator(@PathParam("id") String id) {
        return AdministratorConverter.administratorToAdministratorDTO(service.getAdministrator(new ObjectId(id)));
    }

    @POST
    @Path("/administrators")
    public AdministratorDTO createAdministrator(@Valid AdministratorDTO dto) {
        Administrator saved = service.createUser(AdministratorConverter.administratorDTOToAdministrator(dto));
        return AdministratorConverter.administratorToAdministratorDTO(saved);
    }

    @GET
    @Path("/administrators/by-login/{login}")
    public AdministratorDTO getAdministratorByLogin(@PathParam("login") String login) {
        return AdministratorConverter.administratorToAdministratorDTO(service.getAdministratorByLogin(login));
    }

    @GET
    @Path("/administrators/search")
    public List<AdministratorDTO> searchAdministrators(@QueryParam("q") String q) {
        return service.searchAdministrators(q)
                .stream()
                .map(AdministratorConverter::administratorToAdministratorDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/administrators")
    public List<AdministratorDTO> getAllAdministrators() {
        return service.getAllAdministrators()
                .stream()
                .map(AdministratorConverter::administratorToAdministratorDTO)
                .collect(Collectors.toList());
    }

    @PUT
    @Path("/administrators/{id}")
    public AdministratorDTO updateAdministrator(@PathParam("id") String id, @Valid AdministratorDTO dto) {
        Administrator updated = service.updateAdministrator(new ObjectId(id), AdministratorConverter.administratorDTOToAdministrator(dto));
        return AdministratorConverter.administratorToAdministratorDTO(updated);
    }

    @PATCH
    @Path("/administrators/{id}/activate")
    public AdministratorDTO activateAdministrator(@PathParam("id") String id) {
        return AdministratorConverter.administratorToAdministratorDTO((Administrator) service.activate(new ObjectId(id)));
    }

    @PATCH
    @Path("/administrators/{id}/deactivate")
    public AdministratorDTO deactivateAdministrator(@PathParam("id") String id) {
        return AdministratorConverter.administratorToAdministratorDTO((Administrator) service.deactivate(new ObjectId(id)));
    }

    @GET
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }
}
