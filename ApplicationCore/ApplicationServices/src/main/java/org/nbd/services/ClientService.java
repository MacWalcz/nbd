package org.nbd.services;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.exceptions.UserNotFoundException;
import org.nbd.model.Client;
import org.nbd.ports.input.clients.ClientQueryUseCase;
import org.nbd.ports.output.clients.ClientQueryPort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class ClientService implements ClientQueryUseCase {

    private ClientQueryPort clientQueryPort;

    @Override
    public Client getClient(ObjectId id) {
        return clientQueryPort.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
    @Override
    public Client getClientByLogin(String login) {
        return  clientQueryPort.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
    }
    @Override
    public List<Client> searchClients(String partial) {
        return clientQueryPort.findAllByLoginContainingIgnoreCase(partial);
    }
    @Override
    public List<Client> getAllClients() {
        return clientQueryPort.findAll();
    }

}
