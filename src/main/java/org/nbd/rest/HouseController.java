package org.nbd.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.nbd.converters.HouseConverter;
import org.nbd.dto.HouseDTO;
import org.nbd.model.House;
import org.nbd.services.HouseService;

import java.util.List;
import java.util.stream.Collectors;

import static org.nbd.converters.HouseConverter.houseDTOToHouse;
import static org.nbd.converters.HouseConverter.houseToHouseDTO;

@RequestScoped
@Path("/houses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HouseController {

    @Inject
    private HouseService service;

    @GET
    @Path("/{id}")
    public HouseDTO getHouse(@PathParam("id") String id) {
        House house = service.getHouse(new ObjectId(id));
        return houseToHouseDTO(house);
    }

    @POST
    public HouseDTO postHouse(@Valid HouseDTO dto) {
        House house = houseDTOToHouse(dto);
        House saved = service.createHouse(house);
        return houseToHouseDTO(saved);
    }

    @GET
    public List<HouseDTO> getAll() {
        return service.getAllHouses()
                .stream()
                .map(HouseConverter::houseToHouseDTO)
                .collect(Collectors.toList());
    }

    @PUT
    @Path("/{id}")
    public HouseDTO update(@PathParam("id") String id, @Valid HouseDTO dto) {
        House house = houseDTOToHouse(dto);
        House updated = service.updateHouse(new ObjectId(id), house);
        return houseToHouseDTO(updated);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id) {
        service.deleteHouse(new ObjectId(id));
    }
}
