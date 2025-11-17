package org.nbd.rest;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.nbd.converters.ClientConverter;
import org.nbd.dto.ClientDTO;
import org.nbd.model.Client;
import org.nbd.repositories.ClientRepo;
import org.nbd.services.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.nbd.converters.ClientConverter.clientDTOToClient;
import static org.nbd.converters.ClientConverter.clientToClientDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
public class ClientController {

    private final @NonNull ClientService service;


    @GetMapping("/{id}")
    public ClientDTO getClient(@PathVariable String id) {
        return clientToClientDTO(service.getClient(id));
    }

    @PostMapping
    public ClientDTO postClient(@Valid @RequestBody ClientDTO dto) {
        Client client = clientDTOToClient(dto);
        Client saved = service.createClient(client);
        return clientToClientDTO(saved);
    }

    @GetMapping("/by-login/{login}")
    public Client getExact(@PathVariable String login) {
        return service.getByLogin(login);
    }

    @GetMapping("/search")
    public List<Client> search(@RequestParam String q) {
        return service.searchByLogin(q);
    }

    @GetMapping
    public List<Client> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable String id, @Valid @RequestBody ClientDTO dto) {
        Client client = clientDTOToClient(dto);
        return service.update(id, client);
    }

    @PatchMapping("/{id}/activate")
    public Client activate(@PathVariable String id) {
        return service.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    public Client deactivate(@PathVariable String id) {
        return service.deactivate(id);
    }
}
