package org.nbd.adapters.repositories.aggregates.rents;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.adapters.repositories.RentRepo;
import org.nbd.adapters.repositories.mappers.RentEntMapper;
import org.nbd.adapters.repositories.mappers.UserEntMapper;
import org.nbd.model.Rent;
import org.nbd.ports.output.rents.RentQueryPort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class RentQueryRepositoryAdapter implements RentQueryPort {

    private final RentRepo rentRepo;
    private final RentEntMapper rentEntMapper;

    @Override
    public List<Rent> findAll() {
        return rentRepo.findAll().stream().map(rentEntMapper::toRent).toList();
    }

    @Override
    public Optional<Rent> findById(ObjectId id) {
        return rentRepo.findById(id).map(rentEntMapper::toRent);
    }

    @Override
    public boolean existsActiveOrFutureRent(ObjectId houseId, LocalDate now) {
        return rentRepo.existsActiveOrFutureRent(houseId, now);
    }

    @Override
    public List<Rent> findAllActiveOrFutureRent(LocalDate now) {
        return rentRepo.findAllActiveOrFutureRent(now).stream().map(rentEntMapper::toRent).toList();
    }


    @Override
    public List<Rent> findByClientIdAndEndDateIsNull(ObjectId clientId) {
        return rentRepo.findByClientIdAndEndDateIsNull(clientId).stream().map(rentEntMapper::toRent).toList();
    }

    @Override
    public List<Rent> findByClientIdAndEndDateIsNotNull(ObjectId clientId) {
        return rentRepo.findByClientIdAndEndDateIsNotNull(clientId).stream().map(rentEntMapper::toRent).toList();
    }

    @Override
    public List<Rent> findByHouseIdAndEndDateIsNull(ObjectId houseId) {
        return rentRepo.findByHouseIdAndEndDateIsNull(houseId).stream().map(rentEntMapper::toRent).toList();
    }

    @Override
    public List<Rent> findByHouseIdAndEndDateIsNotNull(ObjectId houseId) {
        return rentRepo.findByHouseIdAndEndDateIsNotNull(houseId).stream().map(rentEntMapper::toRent).toList();
    }
}
