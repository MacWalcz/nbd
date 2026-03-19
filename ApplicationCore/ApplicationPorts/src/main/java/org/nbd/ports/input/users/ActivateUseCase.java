package org.nbd.ports.input.users;

import org.bson.types.ObjectId;
import org.nbd.model.User;

public interface ActivateUseCase {
    User activate(ObjectId id);
    User deactivate(ObjectId id);
}
