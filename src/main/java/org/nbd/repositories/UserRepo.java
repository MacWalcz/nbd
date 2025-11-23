package org.nbd.repositories;

import org.bson.types.ObjectId;
import org.nbd.model.Client;
import org.nbd.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<User, ObjectId> {
    Optional<User> findByLogin(String login);

    List<User> findAllByLoginContainingIgnoreCase(String partial);


    boolean existsByLogin(String login);
}