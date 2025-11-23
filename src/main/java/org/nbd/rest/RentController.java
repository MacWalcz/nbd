package org.nbd.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.converters.RentConverter;
import org.nbd.dto.RentDTO;
import org.nbd.model.Rent;
import org.nbd.services.RentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.nbd.converters.RentConverter.rentToRentDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rents")
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
public class RentController {

    private final RentService service;

    @GetMapping("/{id}")
    public RentDTO getRent(@PathVariable String id) {
        return rentToRentDTO(service.getRent(new ObjectId(id)));
    }

    @PostMapping
    public RentDTO create(@Valid @RequestParam String client,
                          @Valid @RequestParam String house,
                          @Valid @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startTime) {
        Rent rent = service.createRent(new ObjectId(client), new ObjectId(house), startTime);
        return rentToRentDTO(rent);
    }

    @GetMapping("/current/client/{clientId}")
    public List<RentDTO> getCurrentRentsForClient(@PathVariable String clientId) {
        return service.getCurrentRentsForClient(new ObjectId(clientId))
                .stream()
                .map(RentConverter::rentToRentDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/past/client/{clientId}")
    public List<RentDTO> getPastRentsForClient(@PathVariable String clientId) {
        return service.getPastRentsForClient(new ObjectId(clientId))
                .stream()
                .map(RentConverter::rentToRentDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/current/house/{houseId}")
    public List<RentDTO> getCurrentRentsForHouse(@PathVariable String houseId) {
        return service.getCurrentRentsForHouse(new ObjectId(houseId))
                .stream()
                .map(RentConverter::rentToRentDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/past/house/{houseId}")
    public List<RentDTO> getPastRentsForHouse(@PathVariable String houseId) {
        return service.getPastRentsForHouse(new ObjectId(houseId))
                .stream()
                .map(RentConverter::rentToRentDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/end")
    public RentDTO endRent(@PathVariable String id, @RequestParam LocalDate endTime) {
        Rent rent = service.endRent(new ObjectId(id), endTime);
        return rentToRentDTO(rent);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteActiveRent(new ObjectId(id));
    }
}
