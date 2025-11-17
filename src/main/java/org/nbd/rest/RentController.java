package org.nbd.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nbd.converters.RentConverter;
import org.nbd.dto.RentDTO;
import org.nbd.model.Rent;
import org.nbd.services.RentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.nbd.converters.RentConverter.rentToRentDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rents")
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
public class RentController {

    private final RentService service;


    @GetMapping("/{id}")
    public RentDTO getRent(@PathVariable String id) {
        return rentToRentDTO(service.getRent(id));
    }

    @PostMapping
    public Rent create(@Valid @RequestParam String client,
                       @Valid @RequestParam String house,
                       @Valid @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startTime) {
        return service.createRent(client, house, startTime);
    }

    @GetMapping("/current/client/{clientId}")
    public List<Rent> getCurrentRentsForClient(@PathVariable String clientId) {
        return service.getCurrentRentsForClient(clientId);
    }

    @GetMapping("/past/client/{clientId}")
    public List<Rent> getPastRentsForClient(@PathVariable String clientId) {
        return service.getPastRentsForClient(clientId);
    }

    @GetMapping("/current/house/{houseId}")
    public List<Rent> getCurrentRentsForHouse(@PathVariable String houseId) {
        return service.getCurrentRentsForHouse(houseId);
    }

    @GetMapping("/past/house/{houseId}")
    public List<Rent> getPastRentsForHouse(@PathVariable String houseId) {
        return service.getPastRentsForHouse(houseId);
    }

    @PutMapping("/{id}/end")
    public Rent endRent(@PathVariable String id, @RequestParam LocalDate endTime) {
        return service.endRent(id, endTime);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteActiveRent(id);
    }
}

