package org.nbd.adapters.repositories.aggregates.administrators;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.adapters.repositories.AdministratorRepo;
import org.nbd.adapters.repositories.mappers.AdministratorEntMapper;
import org.nbd.model.Administrator;
import org.nbd.ports.output.administrators.AdminQueryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AdministratorQueryRepositoryAdapter implements AdminQueryPort {

    private final AdministratorEntMapper administratorEntMapper;
    private final AdministratorRepo administratorRepo;

    @Override
    public Optional<Administrator> findById(ObjectId id) {
        return administratorRepo.findById(id).map(administratorEntMapper::toAdministrator);
    }

    @Override
    public Optional<Administrator> findByLogin(String login) {
        return administratorRepo.findByLogin(login).map(administratorEntMapper::toAdministrator);
    }

    @Override
    public List<Administrator> findAll() {
        return administratorRepo.findAll().stream().map(administratorEntMapper::toAdministrator).toList();
    }

    @Override
    public List<Administrator> findAllByLoginContainingIgnoreCase(String partial) {
        return administratorRepo.findAllByLoginContainingIgnoreCase(partial).stream().map(administratorEntMapper::toAdministrator).toList();
    }
}
