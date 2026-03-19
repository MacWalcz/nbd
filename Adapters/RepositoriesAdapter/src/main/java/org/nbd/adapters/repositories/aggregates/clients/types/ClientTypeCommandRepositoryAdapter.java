package org.nbd.adapters.repositories.aggregates.clients.types;


import lombok.AllArgsConstructor;
import org.nbd.adapters.repositories.ClientTypeRepo;
import org.nbd.adapters.repositories.mappers.ClientTypeEntMapper;
import org.nbd.model.ClientType;
import org.nbd.ports.output.clients.types.ClientTypeCommandPort;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class ClientTypeCommandRepositoryAdapter implements ClientTypeCommandPort {

    private final ClientTypeEntMapper clientTypeEntMapper;
    private final ClientTypeRepo clientTypeRepo;


    @Override
    public ClientType save(ClientType clientType) {
        return clientTypeEntMapper.toClientType(clientTypeRepo.save(clientTypeEntMapper.toClientTypeEnt(clientType)));
    }
}
