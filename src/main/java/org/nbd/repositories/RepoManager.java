package org.nbd.repositories;

import org.bson.types.ObjectId;
import org.nbd.model.Rent;

import java.util.List;

public interface RepoManager<T> {
    void save(T t);

    T findById(ObjectId id);

    void update(ObjectId id, T t);

    void deleteById(ObjectId id);

    void deleteAll();

    List<T> findAll();
}

