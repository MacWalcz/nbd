package org.nbd.adapters.repositories;

import org.bson.types.ObjectId;
import org.nbd.adapters.repositories.entities.UserEnt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<UserEnt, ObjectId> {
    Optional<UserEnt> findByLogin(String login);

    List<UserEnt> findAllByLoginContainingIgnoreCase(String partial);

    boolean existsByLogin(String login);
}