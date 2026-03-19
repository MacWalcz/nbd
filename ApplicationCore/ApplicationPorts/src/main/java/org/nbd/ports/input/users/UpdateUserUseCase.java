package org.nbd.ports.input.users;

import org.bson.types.ObjectId;
import org.nbd.model.User;

public interface UpdateUserUseCase {
    <T extends User> T updateUser(ObjectId id, T updated);
}
