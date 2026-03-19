package org.nbd.ports.output.clients;

import org.bson.types.ObjectId;
import org.nbd.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientQueryPort {
    Optional<Client> findById(ObjectId id);
    Optional<Client> findByLogin(String login);
    List<Client> findAll();
    List<Client> findAllByLoginContainingIgnoreCase(String partial);
}
