package org.nbd.repositories;

import org.bson.types.ObjectId;
import org.nbd.model.ClientType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientTypeRepo extends MongoRepository<ClientType, ObjectId> {
}
