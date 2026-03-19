package org.nbd.ports.input.rents;

import org.bson.types.ObjectId;
import org.nbd.model.Rent;

import java.util.List;

public interface RentQueryUseCase {
    Rent getRentById(ObjectId id);
    List<Rent> getCurrentRentsForClient(ObjectId clientId);
    List<Rent> getPastRentsForClient(ObjectId clientId);
    List<Rent> getCurrentRentsForHouse(ObjectId houseId);
    List<Rent> getPastRentsForHouse(ObjectId houseId);
    List<Rent> getAllRents();

}
