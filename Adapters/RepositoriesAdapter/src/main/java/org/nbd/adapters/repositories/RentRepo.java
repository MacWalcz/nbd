package org.nbd.adapters.repositories;

import org.apache.logging.log4j.util.InternalException;
import org.bson.types.ObjectId;
import org.nbd.adapters.repositories.entities.HouseEnt;
import org.nbd.adapters.repositories.entities.RentEnt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentRepo extends MongoRepository<RentEnt, ObjectId> {
    @Transactional
    default RentEnt saveRent(RentEnt rent) {
        List<RentEnt> rents = findOverlappingReservations(rent.getHouse().getId(), rent.getStartDate(), rent.getEndDate());
        if (!rents.isEmpty()) {
            throw new InternalException(rent.getHouse().getId().toString());
        }
        return save(rent);
    }

    @Query("{ 'id': ?0, '$or': [ " +
            "{ 'startDate': { '$lte': ?2 }, 'endDate': { '$gte': ?1 } }, " +
            "{ 'startDate': { '$gte': ?1, '$lte': ?2 } } ] }")
    List<RentEnt> findOverlappingReservations(ObjectId id, LocalDate startDate, LocalDate endDate);


    @Query(""" 
            { "houseId": ?0, 
            "$or": [ 
            { "endDate": null }, 
            { "endDate": { "$gt": ?1 } },
            { "startDate":  { "$gt": ?1 } }             
           ] 
            } 
            """)
    boolean existsActiveOrFutureRent(ObjectId houseId, LocalDate now);

    @Query(""" 
            { 
            "$or": [ 
            { "endDate": null }, 
            { "endDate": { "$gt": ?0 } },
            { "startDate":  { "$gt": ?0 } }             
           ] 
            } 
            """)
    List<RentEnt> findAllActiveOrFutureRent(LocalDate now);

    List<RentEnt> findByClientIdAndEndDateIsNull(ObjectId clientId);

    List<RentEnt> findByClientIdAndEndDateIsNotNull(ObjectId clientId);

    List<RentEnt> findByHouseIdAndEndDateIsNull(ObjectId houseId);

    List<RentEnt> findByHouseIdAndEndDateIsNotNull(ObjectId houseId);

    List<RentEnt> findAll();

    List<RentEnt> findAllByEndDateIsNull();

    List<RentEnt> house(HouseEnt house);
}
