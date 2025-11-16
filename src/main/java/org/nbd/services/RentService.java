package org.nbd.services;

import lombok.AllArgsConstructor;
import org.nbd.exceptions.RentNotFoundException;
import org.nbd.model.Rent;
import org.nbd.repositories.RentRepo;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RentService {

    private final RentRepo rentRepo;

    public Rent getRent(String id) {
        return rentRepo.findById(id).orElseThrow(() -> new RentNotFoundException(id));
    }

    public Rent createRent(Rent rent) {
        return rentRepo.save(rent);
    }
}

