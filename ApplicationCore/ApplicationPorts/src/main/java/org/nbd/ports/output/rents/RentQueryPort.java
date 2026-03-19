package org.nbd.ports.output.rents;

import org.bson.types.ObjectId;
import org.nbd.model.House;
import org.nbd.model.Rent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentQueryPort {
    List<Rent> findAll();
    Optional<Rent> findById(ObjectId id);
    boolean existsActiveOrFutureRent(ObjectId houseId, LocalDate now);
    List<Rent> findAllActiveOrFutureRent(LocalDate now);
    List<Rent> findByClientIdAndEndDateIsNull(ObjectId clientId);
    List<Rent> findByClientIdAndEndDateIsNotNull(ObjectId clientId);
    List<Rent> findByHouseIdAndEndDateIsNull(ObjectId houseId);
    List<Rent> findByHouseIdAndEndDateIsNotNull(ObjectId houseId);
}
