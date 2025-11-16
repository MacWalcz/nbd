package org.nbd.converters;

import lombok.RequiredArgsConstructor;
import org.nbd.dto.RentDTO;
import org.nbd.model.Rent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RentConverter {

    public RentDTO rentToRentDTO(Rent rent) {
        return new RentDTO(
            rent.getId(),
            rent.getStartDate(),
            rent.getEndDate(),
            rent.getClient(),
            rent.getHouse()
        );
    }

    public Rent rentDTOToRent(RentDTO dto) {

        return Rent.builder()
                .id(dto.id())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .client(dto.client())
                .house(dto.house())
                .build();
    }
}
