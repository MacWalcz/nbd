package org.nbd.services;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.exceptions.UserNotFoundException;
import org.nbd.model.Administrator;
import org.nbd.ports.input.administrators.AdministratorQueryUseCase;
import org.nbd.ports.output.administrators.AdminQueryPort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class AdministratorService implements AdministratorQueryUseCase {

    private AdminQueryPort adminQueryPort;

    public Administrator getAdministrator(ObjectId id) {
        return adminQueryPort.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public Administrator getAdministratorByLogin(String login) {
        return adminQueryPort.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
    }

    public List<Administrator> searchAdministrators(String partial) {
        return adminQueryPort.findAllByLoginContainingIgnoreCase(partial);
    }

    public List<Administrator> getAllAdministrators() {
        return adminQueryPort.findAll();
    }

}
