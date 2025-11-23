package org.nbd.repositories;

import org.bson.types.ObjectId;
import org.nbd.exceptions.HouseNotAvaibleException;
import org.nbd.model.Rent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RentRepo extends MongoRepository<Rent, ObjectId> {
    @Transactional
    default Rent saveRent(Rent rent) {
        List<Rent> rents = findOverlappingReservations(rent.getHouse().getId(),rent.getStartDate(),rent.getEndDate());
        if (!rents.isEmpty()) {
            throw new HouseNotAvaibleException(rent.getHouse().getId());
        }
        return save(rent);
    }

    @Query("{ 'id': ?0, '$or': [ " +
            "{ 'startDate': { '$lte': ?2 }, 'endDate': { '$gte': ?1 } }, " +
            "{ 'startDate': { '$gte': ?1, '$lte': ?2 } } ] }")
    List<Rent> findOverlappingReservations(ObjectId id, LocalDate startDate, LocalDate endDate);

    boolean existsByHouseIdAndEndDateIsNull(ObjectId houseId);

    List<Rent> findByClientIdAndEndDateIsNull(ObjectId clientId);
    List<Rent> findByClientIdAndEndDateIsNotNull(ObjectId clientId);

    List<Rent> findByHouseIdAndEndDateIsNull(ObjectId houseId);
    List<Rent> findByHouseIdAndEndDateIsNotNull(ObjectId houseId);

}
