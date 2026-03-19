package org.nbd.adapters.repositories.mappers;

import org.mapstruct.Mapper;
import org.nbd.adapters.repositories.entities.AdministratorEnt;
import org.nbd.model.Administrator;

@Mapper(componentModel = "spring")
public interface AdministratorEntMapper {
    AdministratorEnt toAdministratorEnt(Administrator administrator);
    Administrator toAdministrator(AdministratorEnt administratorEnt);
}
