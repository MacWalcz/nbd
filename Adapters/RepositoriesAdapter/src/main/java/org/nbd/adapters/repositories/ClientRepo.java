package org.nbd.adapters.repositories;

import org.bson.types.ObjectId;
import org.nbd.adapters.repositories.entities.ClientEnt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepo extends MongoRepository<ClientEnt, ObjectId> {
    Optional<ClientEnt> findByLogin(String login);

    List<ClientEnt> findAllByLoginContainingIgnoreCase(String partial);


    boolean existsByLogin(String login);
}