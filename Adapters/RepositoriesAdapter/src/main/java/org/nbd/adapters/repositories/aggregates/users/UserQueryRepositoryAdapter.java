package org.nbd.adapters.repositories.aggregates.users;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.adapters.repositories.UserRepo;
import org.nbd.adapters.repositories.mappers.UserEntMapper;
import org.nbd.model.User;
import org.nbd.ports.output.users.UserQueryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserQueryRepositoryAdapter implements UserQueryPort {

    private final UserRepo userRepo;
    private final UserEntMapper userEntMapper;

    @Override
    public Optional<User> findById(ObjectId id) {

        return userRepo.findById(id).map(userEntMapper::toUser);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepo.findByLogin(login).map(userEntMapper::toUser);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll().stream().map(userEntMapper::toUser).toList();
    }

    @Override
    public List<User> findAllByLoginContainingIgnoreCase(String partial) {
        return userRepo.findAllByLoginContainingIgnoreCase(partial).stream().map(userEntMapper::toUser).toList();
    }
}
