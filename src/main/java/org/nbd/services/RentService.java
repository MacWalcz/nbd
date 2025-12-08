package org.nbd.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional; // Используем Jakarta Transactions (или просто jta)
import org.bson.types.ObjectId;
import org.nbd.exceptions.*;
import org.nbd.model.Client;
import org.nbd.model.House;
import org.nbd.model.Rent;
// import org.nbd.repositories.ClientRepo; // УДАЛЕНО
import org.nbd.repositories.UserRepo; // ИСПОЛЬЗУЕМ UserRepo
import org.nbd.repositories.HouseRepo;
import org.nbd.repositories.RentRepo;

import java.time.LocalDate;
import java.util.List;

// Lombok @NoArgsConstructor и @AllArgsConstructor удалены, используем @Inject
@ApplicationScoped
public class RentService {

    @Inject
    private RentRepo rentRepo;

    // Заменяем ClientRepo на UserRepo
    @Inject
    private UserRepo userRepo;

    @Inject
    private HouseRepo houseRepo;

    public Rent getRent(ObjectId id) {
        Rent rent = rentRepo.findById(id);
        if (rent == null) throw new RentNotFoundException(id);
        return rent;
    }

    // Аннотация @Transactional из Jakarta (требует настройки JTA в контейнере)
    @Transactional
    public Rent createRent(ObjectId clientId, ObjectId houseId, LocalDate startDate) {

        // 1. Ищем клиента через UserRepo и приводим тип
        Client client = (Client) userRepo.findById(clientId);

        if (client == null) throw new UserNotFoundException(clientId);

        // Проверка активности клиента (допустим, что client.isActive() доступен)
        if (!client.isActive()) throw new UserInactiveException(clientId);

        House house = houseRepo.findById(houseId);
        if (house == null) throw new HouseNotFoundException(houseId);

        if (rentRepo.existsActiveForHouse(houseId)) {
            throw new HouseActiveRentException(houseId);
        }

        Rent rent = Rent.builder()
                .client(client)
                .house(house)
                .startDate(startDate)
                .build();

        return rentRepo.save(rent);
    }

    public List<Rent> getCurrentRentsForClient(ObjectId clientId) {
        return rentRepo.findByClientIdAndEndDateIsNull(clientId);
    }

    public List<Rent> getPastRentsForClient(ObjectId clientId) {
        return rentRepo.findByClientIdAndEndDateIsNotNull(clientId);
    }

    public List<Rent> getCurrentRentsForHouse(ObjectId houseId) {
        return rentRepo.findByHouseIdAndEndDateIsNull(houseId);
    }

    public List<Rent> getPastRentsForHouse(ObjectId houseId) {
        return rentRepo.findByHouseIdAndEndDateIsNotNull(houseId);
    }

    @Transactional
    public Rent endRent(ObjectId rentId, LocalDate endDate) {
        Rent rent = rentRepo.findById(rentId);
        if (rent == null) throw new RentNotFoundException(rentId);

        if (rent.getCost() != null) throw new RentAlreadyEnded(rentId);

        rent.endRent(endDate);
        return rentRepo.save(rent);
    }

    @Transactional
    public void deleteActiveRent(ObjectId rentId) {
        Rent rent = rentRepo.findById(rentId);
        if (rent == null) throw new RentNotFoundException(rentId);

        if (rent.getEndDate() != null) throw new RentNotFinishedException(rentId); // Использовать rent.getEndDate() != null вместо !rent.isActive()

        rentRepo.deleteById(rentId);
    }

}
