package org.nbd.rest;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.converters.ClientConverter;
import org.nbd.dto.ClientDTO;
import org.nbd.model.Client;
import org.nbd.services.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        return clientToClientDTO(service.getClient(new ObjectId(id)));
    }

    @PostMapping
    public ClientDTO postClient(@Valid @RequestBody ClientDTO dto) {
        Client saved = service.createClient(clientDTOToClient(dto));
        return clientToClientDTO(saved);
    }

    @GetMapping("/by-login/{login}")
    public ClientDTO getExact(@PathVariable String login) {
        return clientToClientDTO(service.getByLogin(login));
    }

    @GetMapping("/search")
    public List<ClientDTO> search(@RequestParam String q) {
        return service.searchByLogin(q)
                .stream()
                .map(ClientConverter::clientToClientDTO)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<ClientDTO> getAll() {
        return service.getAll()
                .stream()
                .map(ClientConverter::clientToClientDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ClientDTO update(@PathVariable String id, @Valid @RequestBody ClientDTO dto) {
        Client updated = service.update(new ObjectId(id), clientDTOToClient(dto));
        return clientToClientDTO(updated);
    }

    @PatchMapping("/{id}/activate")
    public ClientDTO activate(@PathVariable String id) {
        return clientToClientDTO(service.activate(new ObjectId(id)));
    }

    @PatchMapping("/{id}/deactivate")
    public ClientDTO deactivate(@PathVariable String id) {
        return clientToClientDTO(service.deactivate(new ObjectId(id)));
    }
}
