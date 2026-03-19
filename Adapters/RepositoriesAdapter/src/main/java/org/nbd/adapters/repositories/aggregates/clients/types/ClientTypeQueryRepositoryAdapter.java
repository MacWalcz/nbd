package org.nbd.adapters.repositories.aggregates.clients.types;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.adapters.repositories.ClientTypeRepo;
import org.nbd.adapters.repositories.mappers.ClientTypeEntMapper;
import org.nbd.model.ClientType;
import org.nbd.ports.output.clients.types.ClientTypeQueryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ClientTypeQueryRepositoryAdapter implements ClientTypeQueryPort {

    private final ClientTypeEntMapper clientTypeEntMapper;
    private final ClientTypeRepo clientTypeRepo;

    @Override
    public Optional<ClientType> findById(ObjectId id) {
        return clientTypeRepo.findById(id).map(clientTypeEntMapper::toClientType);
    }
}
