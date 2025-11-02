package org.nbd.services;

import lombok.AllArgsConstructor;
import org.nbd.model.Client;
import org.nbd.model.Default;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Component
@Service
public class ClientService {


    public Client getClient(UUID id) {
        return Client.builder()
                .id(id)
                .login("elo")
                .firstName("to")
                .lastName("jest")
                .phoneNumeber("test")
                .clientType(new Default())
                .build();
    }
}
