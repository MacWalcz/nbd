package org.nbd.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.nbd.config.MongoConfig;


@AllArgsConstructor
public abstract class BaseRepo<T> {

    protected final MongoDatabase database;
    protected final MongoCollection<T> collection;
    protected final Class<T> clazz;

    public BaseRepo(MongoConfig config, String collectionName, Class<T> clazz) {
        this.database = config.getDatabase();
        this.clazz = clazz;
        this.collection = database.getCollection(collectionName, clazz);
    }
}
