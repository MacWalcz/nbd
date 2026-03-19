package org.nbd.ports.input.administrators;

import org.bson.types.ObjectId;
import org.nbd.model.Administrator;

import java.util.List;

public interface AdministratorQueryUseCase {
    Administrator getAdministrator(ObjectId id);
    Administrator getAdministratorByLogin(String login);
    List<Administrator> searchAdministrators(String partial);
    List<Administrator> getAllAdministrators();
}
