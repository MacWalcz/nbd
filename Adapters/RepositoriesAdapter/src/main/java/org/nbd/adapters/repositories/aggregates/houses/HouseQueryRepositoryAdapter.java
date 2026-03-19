package org.nbd.adapters.repositories.aggregates.houses;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.adapters.repositories.HouseRepo;
import org.nbd.adapters.repositories.mappers.HouseEntMapper;
import org.nbd.model.House;
import org.nbd.ports.output.houses.HouseQueryPort;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HouseQueryRepositoryAdapter implements HouseQueryPort {

    private final HouseEntMapper houseEntMapper;
    private final HouseRepo houseRepo;

    @Override
    public List<House> findAll() {
        return houseRepo.findAll().stream().map(houseEntMapper::toHouse).collect(Collectors.toList());
    }

    @Override
    public Optional<House> findById(ObjectId id) {
        return houseRepo.findById(id).map(houseEntMapper::toHouse);
    }

}
