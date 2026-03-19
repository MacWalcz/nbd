package org.nbd.ports.output.administrators;

import org.bson.types.ObjectId;
import org.nbd.model.Administrator;


import java.util.List;
import java.util.Optional;

public interface AdminQueryPort {
    Optional<Administrator> findById(ObjectId id);
    Optional<Administrator> findByLogin(String login);
    List<Administrator> findAll();
    List<Administrator> findAllByLoginContainingIgnoreCase(String partial);
}
