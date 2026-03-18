package org.nbd.ports.input.houses;

import org.bson.types.ObjectId;
import org.nbd.model.House;

public interface UpdateHouseUseCase {
    House updateHouse(ObjectId id, House updatedHouse);
}
