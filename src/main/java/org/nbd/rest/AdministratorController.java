package org.nbd.rest;

import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.nbd.converters.AdministratorConverter;
import org.nbd.dto.AdministratorDTO;
import org.nbd.dto.ClientDTO;
import org.nbd.model.Administrator;
import org.nbd.model.Client;
import org.nbd.services.AdministratorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/administrators")
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
public class AdministratorController {

    private final AdministratorService service;
    private final AdministratorConverter converter;

    @GetMapping("/{id}")
    public AdministratorDTO getAdministrator(@PathVariable String id) {
        return converter.administratorToAdministratorDTO(service.getAdministrator(id));
    }

    @PostMapping
    public AdministratorDTO postAdministrator(@Valid @RequestBody AdministratorDTO dto) {
        Administrator admin = converter.administratorDTOToAdministrator(dto);
        Administrator saved = service.createAdministrator(admin);
        return converter.administratorToAdministratorDTO(saved);
    }

    @GetMapping("/by-login/{login}")
    public Administrator getExact(@PathVariable String login) {
        return service.getByLogin(login);
    }

    @GetMapping("/search")
    public List<Administrator> search(@RequestParam String q) {
        return service.searchByLogin(q);
    }

    @GetMapping
    public List<Administrator> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public Administrator update(@PathVariable String id, @Valid @RequestBody AdministratorDTO dto) {
        Administrator administrator = converter.administratorDTOToAdministrator(dto);
        return service.update(id, administrator);
    }

    @PatchMapping("/{id}/activate")
    public Administrator activate(@PathVariable String id) {
        return service.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    public Administrator deactivate(@PathVariable String id) {
        return service.deactivate(id);
    }
}
