package org.nbd.adapters.repositories.aggregates.houses;

import lombok.RequiredArgsConstructor;
import org.nbd.adapters.repositories.HouseRepo;
import org.nbd.adapters.repositories.entities.HouseEnt;
import org.nbd.adapters.repositories.mappers.HouseEntMapper;
import org.nbd.model.House;
import org.nbd.ports.output.houses.HouseCommandPort;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class HouseCommandRepositoryAdapter implements HouseCommandPort{

    private final HouseEntMapper houseEntMapper;
    private final HouseRepo houseRepo;

    @Override
    public House save(House house) {
        HouseEnt houseEnt = houseEntMapper.toHouseEnt(house);
        return houseEntMapper.toHouse(houseRepo.save(houseEnt));

    }

    @Override
    public void delete(House house) {
        HouseEnt houseEnt = houseEntMapper.toHouseEnt(house);
        houseRepo.delete(houseEnt);
    }

}
