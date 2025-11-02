package org.nbd.rest;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.nbd.converters.ClientConverter;
import org.nbd.dto.ClientDTO;
import org.nbd.services.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@CrossOrigin(
        originPatterns = {"http://localhost:[*]"}
)
@RequestMapping(
        value = {"/clients"},
        produces = {"application/json"}
)
public class ClientController {
    private final @NonNull ClientService clientService;

    @GetMapping("{/id}")
    public ClientDTO getClient(@PathVariable UUID id) {
        return ClientConverter.clientToClientDTO(this.clientService.getClient(id));
    }

}
