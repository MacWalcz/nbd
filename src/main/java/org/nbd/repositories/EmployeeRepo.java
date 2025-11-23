package org.nbd.repositories;

import org.bson.types.ObjectId;
import org.nbd.model.Administrator;
import org.nbd.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepo extends MongoRepository<Employee, ObjectId> {
    Optional<Employee> findByLogin(String login);

    List<Employee> findAllByLoginContainingIgnoreCase(String partial);
}
