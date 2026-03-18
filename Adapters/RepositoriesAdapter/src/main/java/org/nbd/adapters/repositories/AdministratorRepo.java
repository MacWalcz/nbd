package org.nbd.adapters.repositories;

import org.bson.types.ObjectId;
import org.nbd.adapters.repositories.entities.AdministratorEnt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdministratorRepo extends MongoRepository<AdministratorEnt, ObjectId> {
    Optional<AdministratorEnt> findByLogin(String login);

    List<AdministratorEnt> findAllByLoginContainingIgnoreCase(String partial);
}
