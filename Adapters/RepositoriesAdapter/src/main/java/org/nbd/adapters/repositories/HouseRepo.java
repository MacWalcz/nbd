package org.nbd.adapters.repositories;

import org.bson.types.ObjectId;
import org.nbd.adapters.repositories.entities.HouseEnt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepo extends MongoRepository<HouseEnt, ObjectId> {
}

