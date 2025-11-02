package org.nbd.converters;

import org.nbd.dto.ClientDTO;
import org.nbd.model.Client;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClientConverter {
    public ClientConverter() {
    }

    public static ClientDTO clientToClientDTO(Client client) {
        return new ClientDTO(client.getId(), client.getLogin(), client.getFirstName(), client.getLastName(), client.getPhoneNumeber(), client.getClientType());
    }

    public static Client clientDTOToClient(ClientDTO clientDTO) {
        return Client.builder()
                .id(clientDTO.id())
                .login(clientDTO.login())
                .firstName(clientDTO.firstName())
                .lastName(clientDTO.lastName())
                .phoneNumeber(clientDTO.phoneNumber())
                .clientType(clientDTO.clientType())
                .build();

    }

    public static List<ClientDTO> clientsToClientDTOs(List<Client> clients) {
        return null == clients ? null : (List)clients.stream().filter(Objects::nonNull).map(ClientConverter::clientToClientDTO).collect(Collectors.toList());
    }


}
