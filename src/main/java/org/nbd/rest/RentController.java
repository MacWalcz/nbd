package org.nbd.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.nbd.converters.RentConverter;
import org.nbd.dto.RentDTO;
import org.nbd.model.Rent;
import org.nbd.services.RentService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.nbd.converters.RentConverter.rentToRentDTO;

@RequestScoped
@Path("/rents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RentController {

    @Inject
    private RentService service;

    @GET
    @Path("/{id}")
    public RentDTO getRent(@PathParam("id") String id) {
        return rentToRentDTO(service.getRent(new ObjectId(id)));
    }

    @POST
    public RentDTO create(@QueryParam("client") String client,
                          @QueryParam("house") String house,
                          @QueryParam("startTime") String startTime) {
        LocalDate startDate = LocalDate.parse(startTime);
        Rent rent = service.createRent(new ObjectId(client), new ObjectId(house), startDate);
        return rentToRentDTO(rent);
    }

    @GET
    @Path("/current/client/{clientId}")
    public List<RentDTO> getCurrentRentsForClient(@PathParam("clientId") String clientId) {
        return service.getCurrentRentsForClient(new ObjectId(clientId))
                .stream()
                .map(RentConverter::rentToRentDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/past/client/{clientId}")
    public List<RentDTO> getPastRentsForClient(@PathParam("clientId") String clientId) {
        return service.getPastRentsForClient(new ObjectId(clientId))
                .stream()
                .map(RentConverter::rentToRentDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/current/house/{houseId}")
    public List<RentDTO> getCurrentRentsForHouse(@PathParam("houseId") String houseId) {
        return service.getCurrentRentsForHouse(new ObjectId(houseId))
                .stream()
                .map(RentConverter::rentToRentDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/past/house/{houseId}")
    public List<RentDTO> getPastRentsForHouse(@PathParam("houseId") String houseId) {
        return service.getPastRentsForHouse(new ObjectId(houseId))
                .stream()
                .map(RentConverter::rentToRentDTO)
                .collect(Collectors.toList());
    }

    @PUT
    @Path("/{id}/end")
    public RentDTO endRent(@PathParam("id") String id, @QueryParam("endTime") String endTime) {
        LocalDate endDate = LocalDate.parse(endTime);
        Rent rent = service.endRent(new ObjectId(id), endDate);
        return rentToRentDTO(rent);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id) {
        service.deleteActiveRent(new ObjectId(id));
    }
}
