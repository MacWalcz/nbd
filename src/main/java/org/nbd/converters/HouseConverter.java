package org.nbd.converters;

import lombok.RequiredArgsConstructor;
import org.nbd.dto.HouseDTO;
import org.nbd.model.House;
import org.springframework.stereotype.Component;


public class HouseConverter {

    public static HouseDTO houseToHouseDTO(House house) {
        return new HouseDTO(
            house.getId(),
            house.getHouseNumber(),
            house.getPrice(),
            house.getArea()
        );
    }

    public static House houseDTOToHouse(HouseDTO dto) {

        return House.builder()
                .id(dto.id())
                .houseNumber(dto.houseNumber())
                .price(dto.price())
                .area(dto.area())
                .build();
    }
}
