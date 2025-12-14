package org.nbd.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.InputStream;
import java.util.Properties;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@ApplicationScoped
public class MongoConfig {

    private final MongoClient client;
    private final String dbName;

    public MongoConfig() {
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) throw new RuntimeException("config.properties not found!");
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load Mongo config", e);
        }

        String uri = props.getProperty("mongo.uri");
        dbName = props.getProperty("mongo.database");

        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .codecRegistry(pojoCodecRegistry)
                .build();

        client = MongoClients.create(settings);
    }

    @Produces
    @ApplicationScoped
    public MongoDatabase mongoDatabase() {
        return client.getDatabase(dbName);
    }

}
