package org.nbd.ports.input.houses;

import org.bson.types.ObjectId;
import org.nbd.model.House;

import java.util.List;

public interface HouseQueryUseCase {
    House getHouse(ObjectId id);
    List<House> getAllHouses();
    List<House> getAvailableHouses();
}
