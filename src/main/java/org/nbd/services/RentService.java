package org.nbd.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.nbd.exceptions.*;
import org.nbd.model.Client;
import org.nbd.model.House;
import org.nbd.model.Rent;
import org.nbd.repositories.HouseRepo;
import org.nbd.repositories.RentRepo;
import org.nbd.repositories.ClientRepo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Component
@Service
public class RentService {

    private final @NonNull RentRepo rentRepo;
    private final @NonNull ClientRepo clientRepo;
    private final @NonNull HouseRepo houseRepo;

    public Rent getRent(String id) {
        return rentRepo.findById(id).orElseThrow(() -> new RentNotFoundException(id));
    }

    public Rent createRent(String clientId, String houseId, LocalDate startDate) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException(clientId));
        if (!client.isActive()) {
            throw new UserInactiveException(clientId);
        }

        House house = houseRepo.findById(houseId)
                .orElseThrow(() -> new HouseNotFoundException(houseId));

        if (rentRepo.existsByHouseIdAndEndDateIsNull(houseId)) {
            throw new HouseActiveRentException(houseId);
        }

        Rent rent = Rent.builder()
                .client(client)
                .house(house)
                .startDate(startDate)
                .build();

        return rentRepo.save(rent);
    }

    public List<Rent> getCurrentRentsForClient(String clientId) {
        return rentRepo.findByClientIdAndEndDateIsNull(clientId);
    }

    public List<Rent> getPastRentsForClient(String clientId) {
        return rentRepo.findByClientIdAndEndDateIsNotNull(clientId);
    }

    public List<Rent> getCurrentRentsForHouse(String houseId) {
        return rentRepo.findByHouseIdAndEndDateIsNull(houseId);
    }

    public List<Rent> getPastRentsForHouse(String houseId) {
        return rentRepo.findByHouseIdAndEndDateIsNotNull(houseId);
    }

    public Rent endRent(String rentId, LocalDate endDate) {
        Rent rent = rentRepo.findById(rentId)
                .orElseThrow(() -> new RentNotFoundException(rentId));
        rent.endRent(endDate);
        return rentRepo.save(rent);
    }

    @Transactional
    public void deleteActiveRent(String rentId) {
        Rent rent = rentRepo.findById(rentId)
                .orElseThrow(() -> new RentNotFoundException(rentId));
        if (!rent.isActive()) {
            throw new RentNotFinishedException(rentId);
        }
        rentRepo.delete(rent);
    }
}

