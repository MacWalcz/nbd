package org.nbd.adapters.repositories.aggregates.rents;

import lombok.AllArgsConstructor;
import org.nbd.adapters.repositories.RentRepo;
import org.nbd.adapters.repositories.mappers.RentEntMapper;
import org.nbd.model.Rent;
import org.nbd.ports.output.rents.RentCommandPort;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RentCommandRepositoryAdapter implements RentCommandPort {

    private RentRepo rentRepo;
    private RentEntMapper rentEntMapper;

    @Override
    public Rent save(Rent rent) {
        return rentEntMapper.toRent(rentRepo.save(rentEntMapper.toRentEnt(rent)));
    }

    @Override
    public void delete(Rent rent) {
        rentRepo.delete(rentEntMapper.toRentEnt(rent));
    }
}
