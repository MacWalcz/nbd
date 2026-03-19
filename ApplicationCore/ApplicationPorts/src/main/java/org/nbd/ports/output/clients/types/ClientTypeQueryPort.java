package org.nbd.ports.output.clients.types;

import org.bson.types.ObjectId;
import org.nbd.model.ClientType;

import java.util.Optional;

public interface ClientTypeQueryPort {
    Optional<ClientType> findById(ObjectId id);
}
