package org.nbd.repositories;

public interface RepoMenager<T> {
    void create(T t);

    T read(long id);

    void update(long id, T t);

    void delete(long id);

}

