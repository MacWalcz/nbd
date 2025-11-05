package org.nbd.repositories;

import org.nbd.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepo extends MongoRepository<Client, UUID> {
}