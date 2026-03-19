package org.nbd.ports.input.users;

import org.nbd.model.User;

public interface CreateUserUseCase {
    <T extends User> T createUser(T user);
}
