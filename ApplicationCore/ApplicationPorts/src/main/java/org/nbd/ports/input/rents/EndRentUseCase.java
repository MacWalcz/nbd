package org.nbd.ports.input.rents;

import org.bson.types.ObjectId;
import org.nbd.model.Rent;

import java.time.LocalDate;

public interface EndRentUseCase {
    Rent endRent(ObjectId rentId, LocalDate endDate);
}
