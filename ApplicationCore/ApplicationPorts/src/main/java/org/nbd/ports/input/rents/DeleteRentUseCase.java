package org.nbd.ports.input.rents;

import org.bson.types.ObjectId;

public interface DeleteRentUseCase {
    void deleteRent(ObjectId rentId);
}
