package org.nbd.services;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.exceptions.*;
import org.nbd.model.Client;
import org.nbd.model.House;
import org.nbd.model.Rent;
import org.nbd.ports.input.rents.*;
import org.nbd.ports.output.clients.ClientQueryPort;
import org.nbd.ports.output.houses.HouseQueryPort;
import org.nbd.ports.output.rents.RentCommandPort;
import org.nbd.ports.output.rents.RentQueryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class RentService implements RentCommandUseCase, RentQueryUseCase {

    private final RentCommandPort rentCommandPort;
    private final RentQueryPort rentQueryPort;
    private final ClientQueryPort clientQueryPort;
    private final HouseQueryPort houseQueryPort;

    @Override
    public Rent getRentById(ObjectId id) {
        return rentQueryPort.findById(id).orElseThrow(() -> new RentNotFoundException(id));
    }

    @Override
    public Rent createRent(ObjectId clientId, ObjectId houseId, LocalDate startDate) {
        Client client = clientQueryPort.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException(clientId));
        if (!client.isActive()) {
            throw new UserInactiveException(clientId);
        }

        House house = houseQueryPort.findById(houseId)
                .orElseThrow(() -> new HouseNotFoundException(houseId));

        if (rentQueryPort.existsActiveOrFutureRent(houseId,LocalDate.now())) {
            throw new HouseActiveRentException(houseId);
        }

        Rent rent = Rent.builder()
                .client(client)
                .house(house)
                .startDate(startDate)
                .build();

        return rentCommandPort.save(rent);
    }

    //do poprawy logika
    @Override
    public List<Rent> getCurrentRentsForClient(ObjectId clientId) {
        return rentQueryPort.findByClientIdAndEndDateIsNull(clientId);
    }

    //do poprawy logika
    @Override
    public List<Rent> getPastRentsForClient(ObjectId clientId) {
        return rentQueryPort.findByClientIdAndEndDateIsNotNull(clientId);
    }


    //tez do poprawy logika
    @Override
    public List<Rent> getCurrentRentsForHouse(ObjectId houseId) {
        return rentQueryPort.findByHouseIdAndEndDateIsNull(houseId);
    }
    //logika poprawa
    @Override
    public List<Rent> getPastRentsForHouse(ObjectId houseId) {
        return rentQueryPort.findByHouseIdAndEndDateIsNotNull(houseId);
    }

    @Override
    public Rent endRent(ObjectId rentId, LocalDate endDate) {
        Rent rent = rentQueryPort.findById(rentId)
                .orElseThrow(() -> new RentNotFoundException(rentId));
        if (endDate.isBefore(rent.getStartDate())) {
            throw new UserInactiveException(rentId);
        }
        if (rent.getCost() != null) {
            throw new RentAlreadyEnded(rentId);
        }
        rent.endRent(endDate);
        return rentCommandPort.save(rent);
    }

    @Transactional
    @Override
    public void deleteRent(ObjectId rentId) {
        Rent rent = rentQueryPort.findById(rentId)
                .orElseThrow(() -> new RentNotFoundException(rentId));
        if (!rent.isActive()) {
            throw new RentNotFinishedException(rentId);
        }
        rentCommandPort.delete(rent);
    }

    @Override
    public List<Rent> getAllRents() {
        return rentQueryPort.findAll();
    }
}

