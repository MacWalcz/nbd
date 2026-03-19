package org.nbd.ports.output.users;

import org.bson.types.ObjectId;
import org.nbd.model.User;

import java.util.List;
import java.util.Optional;

public interface UserQueryPort {
    Optional<User> findById(ObjectId id);
    Optional<User> findByLogin(String login);
    List<User> findAll();
    List<User> findAllByLoginContainingIgnoreCase(String partial);
}
