package org.nbd.rest;

import lombok.RequiredArgsConstructor;
import org.nbd.converters.RentConverter;
import org.nbd.dto.RentDTO;
import org.nbd.model.Rent;
import org.nbd.services.RentService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rents")
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
public class RentController {

    private final RentService service;
    private final RentConverter converter;

    @GetMapping("/{id}")
    public RentDTO getRent(@PathVariable String id) {
        return converter.rentToRentDTO(service.getRent(id));
    }

    @PostMapping
    public RentDTO postRent(@RequestBody RentDTO dto) {
        Rent rent = converter.rentDTOToRent(dto);
        Rent saved = service.createRent(rent);
        return converter.rentToRentDTO(saved);
    }

}

