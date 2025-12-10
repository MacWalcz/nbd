package org.nbd.dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.nbd.model.Client;

@Dao
public interface ClientDao {
    @Insert
    void create(Client client);

    @Query("SELECT * FROM ClientsIds WHERE login = :login")
    Client findAccountByLogin(String login);

    @Delete
    void delete(Client client);

    @Update
    void update(Client client);
}
