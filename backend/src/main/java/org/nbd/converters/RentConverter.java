package org.nbd.converters;

import lombok.RequiredArgsConstructor;
import org.nbd.dto.RentDTO;
import org.nbd.model.Rent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RentConverter {

    public static RentDTO rentToRentDTO(Rent rent) {
        return new RentDTO(
            rent.getId(),
            rent.getStartDate(),
            rent.getEndDate(),
                ClientConverter.clientToClientDTO(rent.getClient()),
                HouseConverter.houseToHouseDTO(rent.getHouse()),
            rent.getCost()
        );
    }

    public static Rent rentDTOToRent(RentDTO dto) {

        return Rent.builder()
                .id(dto.id())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .client(ClientConverter.clientDTOToClient(dto.client()))
                .house(HouseConverter.houseDTOToHouse(dto.house()))
                .cost(dto.cost())
                .build();
    }
}
