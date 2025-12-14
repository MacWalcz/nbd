package org.nbd.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.exceptions.HouseActiveRentException;
import org.nbd.exceptions.HouseNotFoundException;
import org.nbd.model.House;
import org.nbd.repositories.HouseRepo;
import org.nbd.repositories.RentRepo;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ApplicationScoped
public class HouseService {

    @Inject
    private HouseRepo houseRepo;

    @Inject
    private RentRepo rentRepo;

    public House getHouse(ObjectId id) {
        House house = houseRepo.findById(id);
        if (house == null) {
            throw new HouseNotFoundException(id);
        }
        return house;
    }

    @Transactional
    public House createHouse(House house) {
        return houseRepo.save(house);
    }

    public List<House> getAllHouses() {
        return houseRepo.findAll();
    }

    @Transactional
    public House updateHouse(ObjectId id, House updatedHouse) {
        House house = houseRepo.findById(id);
        if (house == null) {
            throw new HouseNotFoundException(id);
        }
        house.setHouseNumber(updatedHouse.getHouseNumber());
        house.setPrice(updatedHouse.getPrice());
        house.setArea(updatedHouse.getArea());
        return houseRepo.update(id, updatedHouse);
    }

    @Transactional
    public void deleteHouse(ObjectId id) {
        House house = houseRepo.findById(id);
        if (house == null) {
            throw new HouseNotFoundException(id);
        }

        if (rentRepo.existsActiveForHouse(id)) {
            throw new HouseActiveRentException(id);
        }

        houseRepo.deleteById(id);
    }
}
