package org.nbd.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.nbd.dto.ClientDTO;
import org.nbd.exceptions.HouseNotFoundException;
import org.nbd.exceptions.LoginAlreadyExists;
import org.nbd.exceptions.UserNotFoundException;
import org.nbd.model.Client;
import org.nbd.model.House;
import org.nbd.repositories.ClientRepo;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Component
@Service
public class ClientService {

    private final @NonNull ClientRepo clientRepo;

    public Client getClient(String id) {
        return clientRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public Client createClient(Client client) {
        try {
            return clientRepo.save(client);
        } catch (DuplicateKeyException e) {
            throw new LoginAlreadyExists(client.getLogin());
        } catch (Exception e) {
            throw new RuntimeException("Wystąpił błąd podczas zapisu użytkownika");
        }


    }

    public Client getByLogin(String login) {
        return clientRepo.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    public List<Client> searchByLogin(String partial) {
        return clientRepo.findAllByLoginContainingIgnoreCase(partial);
    }

    public List<Client> getAll() {
        return clientRepo.findAll();
    }

    public Client update(String id, Client updatedClient) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        client.setLogin(updatedClient.getLogin());
        if (updatedClient.getFirstName() != null) client.setFirstName(updatedClient.getFirstName());
        if (updatedClient.getLastName() != null) client.setLastName(updatedClient.getLastName());
        if (updatedClient.getPhoneNumber() != null) client.setPhoneNumber(updatedClient.getPhoneNumber());
        if (updatedClient.getClientType() != null) client.setClientType(updatedClient.getClientType());
        return clientRepo.save(client);
    }

    public Client activate(String id) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        client.setActive(true);
        return clientRepo.save(client);
    }

    public Client deactivate(String id) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        client.setActive(false);
        return clientRepo.save(client);
    }
}
