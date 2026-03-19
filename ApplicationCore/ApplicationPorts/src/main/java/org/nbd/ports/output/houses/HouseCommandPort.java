package org.nbd.ports.output.houses;

import org.nbd.model.House;

public interface HouseCommandPort {
    House save(House house);

    void delete(House house);
}
