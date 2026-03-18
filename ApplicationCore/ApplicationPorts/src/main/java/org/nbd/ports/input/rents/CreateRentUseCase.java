package org.nbd.ports.input.rents;

import org.bson.types.ObjectId;
import org.nbd.model.Rent;

import java.time.LocalDate;

public interface CreateRentUseCase {
    Rent createRent(ObjectId clientId, ObjectId houseId, LocalDate startDate);
}
