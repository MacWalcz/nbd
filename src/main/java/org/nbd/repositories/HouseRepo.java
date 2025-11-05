package org.nbd.repositories;

import org.nbd.model.House;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HouseRepo extends MongoRepository<House, UUID> {
}

