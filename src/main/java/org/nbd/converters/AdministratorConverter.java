package org.nbd.converters;

import lombok.RequiredArgsConstructor;
import org.nbd.dto.AdministratorDTO;
import org.nbd.model.Administrator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdministratorConverter {

    public AdministratorDTO administratorToAdministratorDTO(Administrator administrator) {
        return new AdministratorDTO(
                administrator.getId(),
                administrator.getLogin(),
                administrator.getFirstName(),
                administrator.getLastName(),
                administrator.getPhoneNumber(),
                administrator.isActive()
        );
    }

    public Administrator administratorDTOToAdministrator(AdministratorDTO dto) {
        return Administrator.builder()
                .id(dto.id())
                .login(dto.login())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .phoneNumber(dto.phoneNumber())
                .active(dto.active())
                .build();
    }
}

