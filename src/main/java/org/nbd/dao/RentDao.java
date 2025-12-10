package org.nbd.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import org.nbd.model.Rent;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Dao
public interface RentDao {

    @Insert
    void save(Rent rent);

    @Select
    Optional<Rent> findByIdAndStartDate(UUID id, LocalDate startDate);

    @Select
    PagingIterable<Rent> findAll();

    @Delete
    void delete(Rent rent);
}