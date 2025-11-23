package org.nbd.repositories;

import org.bson.types.ObjectId;
import org.nbd.model.Administrator;
import org.nbd.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdministratorRepo extends MongoRepository<Administrator, ObjectId> {
    Optional<Administrator> findByLogin(String login);

    List<Administrator> findAllByLoginContainingIgnoreCase(String partial);
}
