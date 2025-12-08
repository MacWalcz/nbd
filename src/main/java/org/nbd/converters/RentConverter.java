package org.nbd.converters;

import org.nbd.dto.RentDTO;
import org.nbd.model.Rent;

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
                .id(dto.getId())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .client(ClientConverter.clientDTOToClient(dto.getClient()))
                .house(HouseConverter.houseDTOToHouse(dto.getHouse()))
                .cost(dto.getCost())
                .build();
    }
}