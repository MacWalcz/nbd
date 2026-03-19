package org.nbd.adapters.repositories.mappers;

import org.mapstruct.Mapper;
import org.nbd.adapters.repositories.entities.RentEnt;
import org.nbd.model.Rent;

@Mapper(componentModel = "spring",uses = {ClientEntMapper.class, HouseEntMapper.class})
public interface RentEntMapper {

    RentEnt toRentEnt(Rent rent);
    Rent toRent(RentEnt rentEnt);

}
