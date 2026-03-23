package org.nbd.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.nbd.converters.RentConverter;
import org.nbd.dto.RentDTO;
import org.nbd.hateoas.RentHateoas;
import org.nbd.model.Rent;
import org.nbd.services.RentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.nbd.hateoas.RentHateoas.toModel;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rents")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.OPTIONS}, allowedHeaders = "*")
public class RentController {

    private final RentService service;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public EntityModel<RentDTO> getRent(@PathVariable String id) {
        Rent rent = service.getRentById(new ObjectId(id));
        return toModel(rent);

    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public EntityModel<RentDTO> create(@Valid @RequestParam String client,
                          @Valid @RequestParam String house,
                          @Valid @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startTime) {
        Rent rent = service.createRent(new ObjectId(client), new ObjectId(house), startTime);
        return toModel(rent);
    }

    @GetMapping("/current/client/{clientId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public CollectionModel<EntityModel<RentDTO>> getCurrentRentsForClient(@PathVariable String clientId) {
        List<EntityModel<RentDTO>> rents = service.getCurrentRentsForClient(new ObjectId(clientId))
                .stream()
                .map(RentHateoas::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(rents);
    }

    @GetMapping("/past/client/{clientId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public List<RentDTO> getPastRentsForClient(@PathVariable String clientId) {
        return service.getPastRentsForClient(new ObjectId(clientId))
                .stream()
                .map(RentConverter::rentToRentDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/current_for_client/client/{clientId}")
    @PreAuthorize("#id == authentication.token.claims['id']")
    public List<RentDTO> getCurrentLohRentsForClient(@PathVariable String clientId) {
        return service.getCurrentRentsForClient(new ObjectId(clientId))
                .stream()
                .map(RentConverter::rentToRentDTO)
                .collect(Collectors.toList());
    }


    @GetMapping("/past_for_client/client/{clientId}")
    @PreAuthorize("#id == authentication.token.claims['id']")
    public List<RentDTO> getPastLohRentsForClient(@PathVariable String clientId) {
        return service.getPastRentsForClient(new ObjectId(clientId))
                .stream()
                .map(RentConverter::rentToRentDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/current/house/{houseId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public List<RentDTO> getCurrentRentsForHouse(@PathVariable String houseId) {
        return service.getCurrentRentsForHouse(new ObjectId(houseId))
                .stream()
                .map(RentConverter::rentToRentDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/past/house/{houseId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public List<RentDTO> getPastRentsForHouse(@PathVariable String houseId) {
        return service.getPastRentsForHouse(new ObjectId(houseId))
                .stream()
                .map(RentConverter::rentToRentDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/end")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE')")
    public EntityModel<RentDTO> endRent(@PathVariable String id, @RequestParam LocalDate endTime) {
        Rent rent = service.endRent(new ObjectId(id), endTime);
        return toModel(rent);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteRent(new ObjectId(id));
        return ResponseEntity.noContent().build(); // HTTP 204
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'EMPLOYEE', 'CLIENT')")
    public CollectionModel<EntityModel<RentDTO>> getAllRents() {
        List<EntityModel<RentDTO>> rents = service.getAllRents()
                .stream()
                .map(RentHateoas::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(rents);
    }
}
