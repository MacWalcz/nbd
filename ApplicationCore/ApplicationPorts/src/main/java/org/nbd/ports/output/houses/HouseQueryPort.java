package org.nbd.ports.output.houses;

import org.bson.types.ObjectId;
import org.nbd.model.House;

import java.util.List;
import java.util.Optional;

public interface HouseQueryPort {
    List<House> findAll();
    Optional<House> findById(ObjectId id);
}
