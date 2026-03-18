package org.nbd.adapters.repositories;

import org.bson.types.ObjectId;
import org.nbd.adapters.repositories.entities.EmployeeEnt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepo extends MongoRepository<EmployeeEnt, ObjectId> {
    Optional<EmployeeEnt> findByLogin(String login);

    List<EmployeeEnt> findAllByLoginContainingIgnoreCase(String partial);
}
