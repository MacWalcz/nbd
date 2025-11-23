package org.nbd.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.converters.HouseConverter;
import org.nbd.dto.HouseDTO;
import org.nbd.model.House;
import org.nbd.services.HouseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.nbd.converters.HouseConverter.houseDTOToHouse;
import static org.nbd.converters.HouseConverter.houseToHouseDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/houses")
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
public class HouseController {

    private final HouseService service;

    @GetMapping("/{id}")
    public HouseDTO getHouse(@PathVariable String id) {
        House house = service.getHouse(new ObjectId(id));
        return houseToHouseDTO(house);
    }

    @PostMapping
    public HouseDTO postHouse(@Valid @RequestBody HouseDTO dto) {
        House house = houseDTOToHouse(dto);
        House saved = service.createHouse(house);
        return houseToHouseDTO(saved);
    }

    @GetMapping
    public List<HouseDTO> getAll() {
        return service.getAllHouses()
                .stream()
                .map(HouseConverter::houseToHouseDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public HouseDTO update(@PathVariable String id, @Valid @RequestBody HouseDTO dto) {
        House house = houseDTOToHouse(dto);
        House updated = service.updateHouse(new ObjectId(id), house);
        return houseToHouseDTO(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteHouse(new ObjectId(id));
    }
}
