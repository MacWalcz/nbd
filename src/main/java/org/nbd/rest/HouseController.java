package org.nbd.rest;

import lombok.RequiredArgsConstructor;
import org.nbd.converters.HouseConverter;
import org.nbd.dto.HouseDTO;
import org.nbd.model.House;
import org.nbd.services.HouseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/houses")
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
public class HouseController {

    private final HouseService service;
    private final HouseConverter converter;

    @GetMapping("/{id}")
    public HouseDTO getHouse(@PathVariable String id) {
        return converter.houseToHouseDTO(service.getHouse(id));
    }

    @PostMapping
    public HouseDTO postHouse(@RequestBody HouseDTO dto) {
        House house = converter.houseDTOToHouse(dto);
        House saved = service.createHouse(house);
        return converter.houseToHouseDTO(saved);
    }

    @GetMapping
    public List<House> getAll() {
        return service.getAllHouses();
    }

    @PutMapping("/{id}")
    public House update(@PathVariable String id, @RequestBody House house) {
        return service.updateHouse(id, house);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteHouse(id);
    }
}

