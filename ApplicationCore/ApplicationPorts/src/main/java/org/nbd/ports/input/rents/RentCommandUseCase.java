package org.nbd.ports.input.rents;

import org.nbd.ports.input.houses.CreateHouseUseCase;
import org.nbd.ports.input.houses.DeleteHouseUseCase;
import org.nbd.ports.input.houses.UpdateHouseUseCase;

public interface RentCommandUseCase extends CreateRentUseCase, DeleteRentUseCase,EndRentUseCase {
}
