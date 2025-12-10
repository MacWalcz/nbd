package org.nbd.mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import org.nbd.dao.ClientDao;
import org.nbd.dao.HouseDao;
import org.nbd.dao.RentDao;

@Mapper
public interface AppMapper {

    @DaoFactory
    ClientDao clientDao();

    @DaoFactory
    HouseDao houseDao();

    @DaoFactory
    RentDao rentDao();

    // @DaoFactory
    // ClientTypeDao clientTypeDao();
}