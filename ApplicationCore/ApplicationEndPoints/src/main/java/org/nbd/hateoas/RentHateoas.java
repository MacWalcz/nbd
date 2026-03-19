package org.nbd.hateoas;

import org.nbd.converters.RentConverter;
import org.nbd.dto.RentDTO;
import org.nbd.model.Rent;
import org.nbd.model.Client;
import org.nbd.model.House;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.nbd.rest.RentController;
import org.nbd.rest.UserController;
import org.nbd.rest.HouseController;

public class RentHateoas {

    public static EntityModel<RentDTO> toModel(Rent rent) {
        EntityModel<RentDTO> model = EntityModel.of(RentConverter.rentToRentDTO(rent));


        model.add(linkTo(methodOn(RentController.class).getRent(rent.getId().toHexString()))
                .withSelfRel());


        if (rent.isActive()) {
            model.add(linkTo(methodOn(RentController.class)
                    .endRent(rent.getId().toHexString(), rent.getEndDate()))
                    .withRel("end"));
        }


        model.add(linkTo(methodOn(RentController.class).delete(rent.getId().toHexString()))
                .withRel("delete"));


        Client client = rent.getClient();
        if (client != null) {
            model.add(linkTo(methodOn(UserController.class)
                    .getClient(client.getId().toHexString()))
                    .withRel("user"));
        }


        House house = rent.getHouse();
        if (house != null) {
            model.add(linkTo(methodOn(HouseController.class)
                    .getHouse(house.getId().toHexString()))
                    .withRel("house"));
        }

        return model;
    }
}
