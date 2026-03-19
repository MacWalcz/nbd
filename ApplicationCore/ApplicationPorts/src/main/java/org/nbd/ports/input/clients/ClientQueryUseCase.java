package org.nbd.ports.input.clients;

import org.bson.types.ObjectId;
import org.nbd.model.Client;

import java.util.List;

public interface ClientQueryUseCase {
    Client getClient(ObjectId id);
    Client getClientByLogin(String login);
    List<Client> searchClients(String partial);
    List<Client> getAllClients();
}
