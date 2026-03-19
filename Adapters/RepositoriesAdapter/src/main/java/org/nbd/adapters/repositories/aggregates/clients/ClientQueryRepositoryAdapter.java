package org.nbd.adapters.repositories.aggregates.clients;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.adapters.repositories.ClientRepo;
import org.nbd.adapters.repositories.RentRepo;
import org.nbd.adapters.repositories.mappers.ClientEntMapper;
import org.nbd.model.Client;
import org.nbd.ports.output.clients.ClientQueryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ClientQueryRepositoryAdapter implements ClientQueryPort {

    private final ClientRepo clientRepo;
    private final ClientEntMapper  clientEntMapper;

    @Override
    public Optional<Client> findById(ObjectId id) {
        return clientRepo.findById(id).map(clientEntMapper::toClient);
    }

    @Override
    public Optional<Client> findByLogin(String login) {
        return clientRepo.findByLogin(login).map(clientEntMapper::toClient);
    }

    @Override
    public List<Client> findAll() {
        return clientRepo.findAll().stream().map(clientEntMapper::toClient).toList();
    }

    @Override
    public List<Client> findAllByLoginContainingIgnoreCase(String partial) {
        return clientRepo.findAllByLoginContainingIgnoreCase(partial).stream().map(clientEntMapper::toClient).toList();
    }
}
