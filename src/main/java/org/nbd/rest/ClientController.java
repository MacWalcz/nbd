package org.nbd.rest;

import lombok.RequiredArgsConstructor;
import org.nbd.converters.ClientConverter;
import org.nbd.dto.ClientDTO;
import org.nbd.model.Client;
import org.nbd.services.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
public class ClientController {

    private final ClientService service;
    private final ClientConverter converter;

    @GetMapping("/{id}")
    public ClientDTO getClient(@PathVariable String id) {
        return converter.clientToClientDTO(service.getClient(id));
    }

    @PostMapping
    public ClientDTO postClient(@RequestBody ClientDTO dto) {
        Client client = converter.clientDTOToClient(dto);
        Client saved = service.createClient(client);
        return converter.clientToClientDTO(saved);
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
    public Client update(@PathVariable String id, @RequestBody ClientDTO dto) {
        Client client = converter.clientDTOToClient(dto);
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
