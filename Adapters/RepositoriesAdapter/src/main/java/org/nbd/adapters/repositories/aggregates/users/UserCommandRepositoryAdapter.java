package org.nbd.adapters.repositories.aggregates.users;

import lombok.AllArgsConstructor;
import org.nbd.adapters.repositories.UserRepo;
import org.nbd.adapters.repositories.mappers.UserEntMapper;
import org.nbd.model.User;
import org.nbd.ports.output.users.UserCommandPort;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserCommandRepositoryAdapter implements UserCommandPort {

    private UserRepo userRepo;
    private final UserEntMapper userEntMapper;

    @Override
    public User save(User user) {
        return userEntMapper.toUser(userRepo.save(userEntMapper.toUserEnt(user)));
    }

    @Override
    public void delete(User user) {
        userRepo.delete(userEntMapper.toUserEnt(user));
    }
}
