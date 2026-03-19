package org.nbd.ports.input.users;

import org.bson.types.ObjectId;

public interface ChangePasswordUseCase {
    void changePassword(ObjectId id, String newPassword);
}
