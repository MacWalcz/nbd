package org.nbd.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.inject.Inject;

import java.lang.reflect.ParameterizedType;

public abstract class BaseRepo<T> {

    protected final MongoDatabase database;
    protected final MongoCollection<T> collection;
    protected final Class<T> clazz;
    protected final String collectionName;

    // Конструктор по умолчанию (protected), необходимый для проксирования CDI
    protected BaseRepo() {
        this.database = null;
        this.collection = null;
        this.clazz = null;
        this.collectionName = null;
    }

    @Inject
    public BaseRepo(MongoDatabase database) {
        this.database = database;

        this.clazz = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.collectionName = clazz.getSimpleName().toLowerCase() + "s";

        this.collection = database.getCollection(collectionName, clazz);
    }
}