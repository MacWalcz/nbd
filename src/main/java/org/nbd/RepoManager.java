package org.nbd;

public interface RepoManager<T> {

    void create(T t);

    T read();

    void update(T t);

    void delete(T t);

}
