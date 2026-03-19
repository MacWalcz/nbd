package org.nbd.ports.output.users;

import org.nbd.model.User;

public interface UserCommandPort {
    User save(User user);
    void delete(User user);
}
