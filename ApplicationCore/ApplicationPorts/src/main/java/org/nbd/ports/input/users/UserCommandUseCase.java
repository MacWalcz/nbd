package org.nbd.ports.input.users;

import org.nbd.ports.input.houses.UpdateHouseUseCase;
import org.nbd.ports.input.rents.CreateRentUseCase;
import org.nbd.ports.input.rents.DeleteRentUseCase;

public interface UserCommandUseCase extends ActivateUseCase, ChangePasswordUseCase, CreateUserUseCase, UpdateUserUseCase {
}
