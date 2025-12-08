package org.nbd.converters;

import org.bson.types.ObjectId;
import org.nbd.dto.ClientDTO;
import org.nbd.model.Client;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClientConverter {

    public static ClientDTO clientToClientDTO(Client client) {
        return new ClientDTO(
                client.getId().toHexString(),
                client.getLogin(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhoneNumber(),
                client.isActive()
        );
    }

    public static Client clientDTOToClient(ClientDTO dto) {
        return Client.builder()
                .id(dto.getId() != null && !dto.getId().isBlank() ? new ObjectId(dto.getId()) : null)
                .login(dto.getLogin())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

    public static List<ClientDTO> clientsToClientDTOs(List<Client> clients) {
        return clients == null ? null :
                clients.stream()
                        .filter(Objects::nonNull)
                        .map(ClientConverter::clientToClientDTO)
                        .collect(Collectors.toList());
    }
}