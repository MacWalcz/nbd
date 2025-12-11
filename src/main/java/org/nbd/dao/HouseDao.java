package org.nbd.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import org.nbd.model.House;
import org.nbd.providers.HouseQueryProvider;

import java.util.Optional;
import java.util.UUID;

@Dao
public interface HouseDao {

    @StatementAttributes(consistencyLevel = "ALL")
    @Insert
    void create(House house);


    @StatementAttributes(consistencyLevel = "ALL")
    @QueryProvider(
            providerClass = HouseQueryProvider.class,
            entityHelpers = { House.class }
    )
    Optional<House> findById(UUID id);


    @StatementAttributes(consistencyLevel = "ALL")
    @Select
    PagingIterable<House> findAll();

    @StatementAttributes(consistencyLevel = "ALL")
    @Delete
    void delete(House house);

    @StatementAttributes(consistencyLevel = "ALL")
    @Update
    void update(House house);
}