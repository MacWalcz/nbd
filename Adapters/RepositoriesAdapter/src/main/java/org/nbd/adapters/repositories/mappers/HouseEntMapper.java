package org.nbd.adapters.repositories.mappers;

import org.mapstruct.Mapper;
import org.nbd.adapters.repositories.entities.HouseEnt;
import org.nbd.model.House;

@Mapper(componentModel = "spring")
public interface HouseEntMapper {
    HouseEnt toHouseEnt(House house);
    House toHouse(HouseEnt houseEnt);
}
