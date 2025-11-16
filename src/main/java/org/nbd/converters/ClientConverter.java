package org.nbd.converters;

import lombok.RequiredArgsConstructor;
import org.nbd.dto.ClientDTO;
import org.nbd.model.Client;
import org.nbd.model.ClientType;
import org.nbd.repositories.ClientTypeRepo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientConverter {

    private final ClientTypeRepo repo;

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
                .orElseThrow(() -> new RuntimeException("Invalid client type: " + dto.clientType()));

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
}
