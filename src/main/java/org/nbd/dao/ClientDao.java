package org.nbd.dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.nbd.model.Client;
import org.nbd.model.House;
import org.nbd.providers.ClientQueryProvider;
import org.nbd.providers.HouseQueryProvider;

import java.util.Optional;
import java.util.UUID;

@Dao
public interface ClientDao {

    @StatementAttributes(consistencyLevel = "ALL")
    @Insert
    void create(Client client);

    @StatementAttributes(consistencyLevel = "ALL")
    @QueryProvider(
            providerClass = ClientQueryProvider.class,
            entityHelpers = { Client.class }
    )
    Optional<Client> findById(UUID id);

    @StatementAttributes(consistencyLevel = "ALL")
    @Delete
    void delete(Client client);

    @StatementAttributes(consistencyLevel = "ALL")
    @Update
    void update(Client client);
}
