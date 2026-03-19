package org.nbd.adapters.repositories.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;
import org.nbd.adapters.repositories.entities.*;
import org.nbd.model.*;

@Mapper(componentModel = "spring")
public interface ClientTypeEntMapper {



    DefaultEnt toClientTypeEnt(Default clientType);
    LuxuryEnt toClientTypeEnt(Luxury clientType);
    PremiumEnt toClientTypeEnt(Premium clientType);

    Default toClientType(DefaultEnt clientTypeEnt);
    Luxury toClientType(LuxuryEnt clientTypeEnt);
    Premium toClientType(PremiumEnt clientTypeEnt);


    default ClientTypeEnt toClientTypeEnt(ClientType clientType) {
        if (clientType instanceof Default d) return toClientTypeEnt(d);
        if (clientType instanceof Luxury l) return toClientTypeEnt(l);
        if (clientType instanceof Premium p) return toClientTypeEnt(p);
        throw new IllegalArgumentException("Unknown ClientType subclass: " + clientType.getClass());
    }

    default ClientType toClientType(ClientTypeEnt clientTypeEnt) {
        if (clientTypeEnt instanceof DefaultEnt d) return toClientType(d);
        if (clientTypeEnt instanceof LuxuryEnt l) return toClientType(l);
        if (clientTypeEnt instanceof PremiumEnt p) return toClientType(p);
        throw new IllegalArgumentException("Unknown ClientTypeEnt subclass: " + clientTypeEnt.getClass());
    }
}
