package org.nbd.converters;

import lombok.RequiredArgsConstructor;
import org.nbd.dto.ClientDTO;
import org.nbd.exceptions.InvalidClientType;
import org.nbd.model.Client;
import org.nbd.model.ClientType;
import org.nbd.repositories.ClientRepo;
import org.nbd.repositories.ClientTypeRepo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientConverter {

    private final ClientTypeRepo repo;
    private final ClientRepo repoClient;

    public ClientDTO clientToClientDTO(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getLogin(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhoneNumber(),
                client.isActive(),
                client.getClientType().getId()
        );
    }

    public Client clientDTOToClient(ClientDTO dto) {

        ClientType type = repo.findById(dto.clientType())
                .orElseThrow(() -> new InvalidClientType(dto.clientType()));

        return Client.builder()
                .id(dto.id())
                .login(dto.login())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .phoneNumber(dto.phoneNumber())
                .active(dto.active())
                .clientType(type)
                .build();
    }

    public Client clientDTOToClient(ClientDTO dto, String id) {

        Client found = repoClient.findById(id).orElse(null);

        return Client.builder()
                .id(found.getId())
                .login(dto.login())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .phoneNumber(dto.phoneNumber())
                .active(found.isActive())
                .clientType(found.getClientType())
                .build();
    }
}
