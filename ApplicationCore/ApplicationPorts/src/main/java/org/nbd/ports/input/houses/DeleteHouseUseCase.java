package org.nbd.ports.input.houses;

import org.bson.types.ObjectId;

public interface DeleteHouseUseCase {
    void deleteHouse(ObjectId id);
}
