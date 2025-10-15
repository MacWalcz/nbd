package org.nbd;

public interface RepoManager<T> {

    void create(T t);

    T read(long id);

    void update(long id, T t);

    void delete(long id);

}
