package org.nbd.services;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.exceptions.HouseActiveRentException;
import org.nbd.exceptions.HouseNotFoundException;
import org.nbd.model.House;
import org.nbd.model.Rent;
import org.nbd.ports.input.houses.*;
import org.nbd.ports.output.houses.HouseCommandPort;
import org.nbd.ports.output.houses.HouseQueryPort;
import org.nbd.ports.output.rents.RentQueryPort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
@Service
public class HouseService implements HouseCommandUseCase, HouseQueryUseCase {

    private final HouseQueryPort houseQueryPort;
    private final HouseCommandPort houseCommandPort;
    private final RentQueryPort rentQueryPort;

    public House getHouse(ObjectId id) {
        return houseQueryPort.findById(id).orElseThrow(() -> new HouseNotFoundException(id));
    }

    public House createHouse(House house) {
        return houseCommandPort.save(house);
    }

    public List<House> getAllHouses() {
        return houseQueryPort.findAll();
    }

    public House updateHouse(ObjectId id, House updatedHouse) {
        House house = houseQueryPort.findById(id)
                .orElseThrow(() -> new HouseNotFoundException(id));
        house.setHouseNumber(updatedHouse.getHouseNumber());
        house.setPrice(updatedHouse.getPrice());
        house.setArea(updatedHouse.getArea());
        return houseCommandPort.save(house);
    }

    @Transactional
    public void deleteHouse(ObjectId id) {
        House house = houseQueryPort.findById(id)
                .orElseThrow(() -> new HouseNotFoundException(id));



        if (rentQueryPort.existsActiveOrFutureRent(id, LocalDate.now())) {
            throw new HouseActiveRentException(id);
        }

        houseCommandPort.delete(house);
    }

    // do poprawy logika (wstępnie poprawiona)
    public List<House> getAvailableHouses() {
        List<Rent> activeRents = rentQueryPort.findAllActiveOrFutureRent(LocalDate.now());

        Set<ObjectId> occupiedHouseIds = activeRents.stream()
                .map(rent -> rent.getHouse().getId())
                .collect(Collectors.toSet());

        return houseQueryPort.findAll().stream()
                .filter(house -> !occupiedHouseIds.contains(house.getId()))
                .toList();
    }
}

