package org.nbd.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import org.nbd.model.House;

import java.util.Optional;
import java.util.UUID;

@Dao
public interface HouseDao {

    @Insert
    void save(House house);

    // Получить дом по его ID (Partition Key)
    @Select
    Optional<House> findById(UUID id);

    // Получить все дома
    @Select
    PagingIterable<House> findAll();

    @Delete
    void delete(House house);
}