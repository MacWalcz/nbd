package org.nbd.services;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.exceptions.LoginAlreadyExists;
import org.nbd.exceptions.UserNotFoundException;
import org.nbd.model.*;
import org.nbd.ports.input.users.*;
import org.nbd.ports.output.users.UserCommandPort;
import org.nbd.ports.output.users.UserQueryPort;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService implements UserQueryUseCase, UserCommandUseCase {

    private final UserCommandPort userCommandPort;
    private final UserQueryPort userQueryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUser(ObjectId id) {
        return userQueryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public <T extends User> T createUser(T user) {
        try {
            if (user.getPassword().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hasło nie może być puste przy tworzeniu!");
            }
            return (T) userCommandPort.save(user);
        } catch (DuplicateKeyException e) {
            throw new LoginAlreadyExists(user.getLogin());
        }
    }

    @Override
    public User getByLogin(String login) {
        return userQueryPort.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    @Override
    public List<User> searchByLogin(String partial) {
        return userQueryPort.findAllByLoginContainingIgnoreCase(partial);
    }

    @Override
    public List<User> getAllUsers() {
        return userQueryPort.findAll();
    }

    //xd co to jest
    @Override
    public <T extends User> T updateUser(ObjectId id, T updated) {

        User user = getUser(id);

        user.setLogin(updated.getLogin());
        user.setFirstName(updated.getFirstName());
        user.setLastName(updated.getLastName());
        user.setPhoneNumber(updated.getPhoneNumber());

        if (user instanceof Client && updated instanceof Client c) {
            ((Client) user).setClientType(c.getClientType());
        }

        if (user instanceof Employee existingEmployee && updated instanceof Employee incomingEmployee) {
            existingEmployee.setPosition("lol");

        }

        return (T) userCommandPort.save(user);
    }

    @Override
    public User activate(ObjectId id) {
        User user = userQueryPort.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setActive(true);
        return userCommandPort.save(user);
    }

    @Override
    public User deactivate(ObjectId id) {
        User user = userQueryPort.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setActive(false);
        return userCommandPort.save(user);
    }

    @Override
    public void changePassword(ObjectId id, String newPassword) {
        User user = userQueryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setPassword(passwordEncoder.encode(newPassword));
        userCommandPort.save(user);
    }
}
