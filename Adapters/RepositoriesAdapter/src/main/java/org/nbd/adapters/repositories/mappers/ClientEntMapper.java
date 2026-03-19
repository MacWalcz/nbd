package org.nbd.adapters.repositories.mappers;

import org.mapstruct.Mapper;
import org.nbd.adapters.repositories.entities.ClientEnt;
import org.nbd.model.Client;

@Mapper(componentModel = "spring")
public interface ClientEntMapper {

    ClientEnt toClientEnt(Client client);
    Client toClient(ClientEnt clientEnt);

}
