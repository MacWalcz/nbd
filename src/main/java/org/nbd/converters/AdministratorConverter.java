package org.nbd.converters;

import org.bson.types.ObjectId;
import org.nbd.dto.AdministratorDTO;
import org.nbd.model.Administrator;

public class AdministratorConverter {

    public static AdministratorDTO administratorToAdministratorDTO(Administrator administrator) {
        return new AdministratorDTO(
                administrator.getId().toString(),
                administrator.getLogin(),
                administrator.getFirstName(),
                administrator.getLastName(),
                administrator.getPhoneNumber(),
                administrator.isActive()
        );
    }

    public static Administrator administratorDTOToAdministrator(AdministratorDTO dto) {
        return Administrator.builder()
                .id(dto.getId() != null && !dto.getId().isBlank() ? new ObjectId(dto.getId()) : null)
                .login(dto.getLogin())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }
}

