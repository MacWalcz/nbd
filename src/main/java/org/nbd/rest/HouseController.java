package org.nbd.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nbd.converters.HouseConverter;
import org.nbd.dto.HouseDTO;
import org.nbd.model.House;
import org.nbd.services.HouseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return houseToHouseDTO(service.getHouse(id));
    }

    @PostMapping
    public HouseDTO postHouse(@Valid @RequestBody HouseDTO dto) {
        House house = houseDTOToHouse(dto);
        House saved = service.createHouse(house);
        return houseToHouseDTO(saved);
    }

    @GetMapping
    public List<House> getAll() {
        return service.getAllHouses();
    }

    @PutMapping("/{id}")
    public House update(@PathVariable String id, @Valid @RequestBody House house) {
        return service.updateHouse(id, house);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteHouse(id);
    }
}

