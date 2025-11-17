package org.nbd.services;

import lombok.AllArgsConstructor;
import org.nbd.exceptions.HouseActiveRentException;
import org.nbd.exceptions.HouseNotFoundException;
import org.nbd.model.House;
import org.nbd.repositories.HouseRepo;
import org.nbd.repositories.RentRepo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Component
@Service
public class HouseService {

    private final HouseRepo houseRepo;
    private final RentRepo rentRepo;

    public House getHouse(String id) {
        return houseRepo.findById(id).orElseThrow(() -> new HouseNotFoundException(id));
    }

    public House createHouse(House house) {
        return houseRepo.save(house);
    }

    public List<House> getAllHouses() {
        return houseRepo.findAll();
    }

    public House updateHouse(String id, House updatedHouse) {
        House house = houseRepo.findById(id)
                .orElseThrow(() -> new HouseNotFoundException(id));
        house.setHouseNumber(updatedHouse.getHouseNumber());
        house.setPrice(updatedHouse.getPrice());
        house.setArea(updatedHouse.getArea());
        return houseRepo.save(house);
    }

    @Transactional
    public void deleteHouse(String id) {
        House house = houseRepo.findById(id)
                .orElseThrow(() -> new HouseNotFoundException(id));

        if (rentRepo.existsByHouseIdAndEndDateIsNull(id)) {
            throw new HouseActiveRentException(id);
        }

        houseRepo.delete(house);
    }
}

