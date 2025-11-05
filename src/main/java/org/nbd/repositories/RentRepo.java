package org.nbd.repositories;

import org.nbd.model.Rent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentRepo extends MongoRepository<Rent, String> {

}
