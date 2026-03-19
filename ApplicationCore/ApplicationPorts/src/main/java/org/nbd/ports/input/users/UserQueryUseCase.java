package org.nbd.ports.input.users;

import org.bson.types.ObjectId;
import org.nbd.model.User;

import java.util.List;

public interface UserQueryUseCase {
    User getUser(ObjectId id);
    User getByLogin(String login);
    List<User> searchByLogin(String partial);
    List<User> getAllUsers();
}
