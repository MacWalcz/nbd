package org.nbd.services;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.exceptions.LoginAlreadyExists;
import org.nbd.exceptions.UserNotFoundException;
import org.nbd.model.Administrator;
import org.nbd.model.Client;
import org.nbd.repositories.AdministratorRepo;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Component
@Service
public class AdministratorService {

    private final AdministratorRepo administratorRepo;

    public Administrator getAdministrator(ObjectId id) {
        return administratorRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public Administrator createAdministrator(Administrator administrator) {
        try {
            return administratorRepo.save(administrator);
        } catch (DuplicateKeyException e) {
            throw new LoginAlreadyExists(administrator.getLogin());
        }
    }

    public Administrator getByLogin(String login) {
        return administratorRepo.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    public List<Administrator> searchByLogin(String partial) {
        return administratorRepo.findAllByLoginContainingIgnoreCase(partial);
    }

    public List<Administrator> getAll() {
        return administratorRepo.findAll();
    }

    public Administrator update(ObjectId id, Administrator updatedAdmin) {
        Administrator admin = administratorRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        admin.setLogin(updatedAdmin.getLogin());
        admin.setFirstName(updatedAdmin.getFirstName());
        if (updatedAdmin.getLastName() != null) admin.setLastName(updatedAdmin.getLastName());
        if (updatedAdmin.getPhoneNumber() != null) admin.setPhoneNumber(updatedAdmin.getPhoneNumber());

        return administratorRepo.save(admin);
    }

    public Administrator activate(ObjectId id) {
        Administrator admin = administratorRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        admin.setActive(true);
        return administratorRepo.save(admin);
    }

    public Administrator deactivate(ObjectId id) {
        Administrator admin = administratorRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        admin.setActive(false);
        return administratorRepo.save(admin);
    }
}
