package org.nbd.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.converters.AdministratorConverter;
import org.nbd.dto.AdministratorDTO;
import org.nbd.model.Administrator;
import org.nbd.services.AdministratorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.nbd.converters.AdministratorConverter.administratorDTOToAdministrator;
import static org.nbd.converters.AdministratorConverter.administratorToAdministratorDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/administrators")
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
public class AdministratorController {

    private final AdministratorService service;

    @GetMapping("/{id}")
    public AdministratorDTO getAdministrator(@PathVariable String id) {
        return administratorToAdministratorDTO(service.getAdministrator(new ObjectId(id)));
    }

    @PostMapping
    public AdministratorDTO postAdministrator(@Valid @RequestBody AdministratorDTO dto) {
        Administrator saved = service.createAdministrator(administratorDTOToAdministrator(dto));
        return administratorToAdministratorDTO(saved);
    }

    @GetMapping("/by-login/{login}")
    public AdministratorDTO getExact(@PathVariable String login) {
        return administratorToAdministratorDTO(service.getByLogin(login));
    }

    @GetMapping("/search")
    public List<AdministratorDTO> search(@RequestParam String q) {
        return service.searchByLogin(q)
                .stream()
                .map(AdministratorConverter::administratorToAdministratorDTO)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<AdministratorDTO> getAll() {
        return service.getAll()
                .stream()
                .map(AdministratorConverter::administratorToAdministratorDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public AdministratorDTO update(@PathVariable String id, @Valid @RequestBody AdministratorDTO dto) {
        Administrator updated = service.update(new ObjectId(id), administratorDTOToAdministrator(dto));
        return administratorToAdministratorDTO(updated);
    }

    @PatchMapping("/{id}/activate")
    public AdministratorDTO activate(@PathVariable String id) {
        return administratorToAdministratorDTO(service.activate(new ObjectId(id)));
    }

    @PatchMapping("/{id}/deactivate")
    public AdministratorDTO deactivate(@PathVariable String id) {
        return administratorToAdministratorDTO(service.deactivate(new ObjectId(id)));
    }
}
