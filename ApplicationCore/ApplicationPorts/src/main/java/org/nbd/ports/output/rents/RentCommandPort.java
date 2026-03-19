package org.nbd.ports.output.rents;

import org.nbd.model.Rent;

public interface RentCommandPort {
    Rent save(Rent rent);
    void delete(Rent rent);
}
