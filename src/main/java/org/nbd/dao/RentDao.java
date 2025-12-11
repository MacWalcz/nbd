package org.nbd.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import org.nbd.model.Rent;
import org.nbd.providers.RentQueryProvider;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Dao
public interface RentDao {

    @StatementAttributes(consistencyLevel = "ALL")
    @Insert
    void create(Rent rent);

    @StatementAttributes(consistencyLevel = "ALL")
    @QueryProvider(
            providerClass = RentQueryProvider.class,
            entityHelpers = { Rent.class }
    )
    Optional<Rent> findByIdAndStartDate(UUID id, LocalDate startDate);

    @StatementAttributes(consistencyLevel = "ALL")
    @Select
    PagingIterable<Rent> findAll();


    @StatementAttributes(consistencyLevel = "ALL")
    @Delete
    void delete(Rent rent);

    @StatementAttributes(consistencyLevel = "ALL")
    @Update
    void update(Rent rent);
}