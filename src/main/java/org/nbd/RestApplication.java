package org.nbd;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.nbd.rest.HouseController;
import org.nbd.rest.RentController;
import org.nbd.rest.UserController;

import java.util.Set;
import java.util.HashSet;




@ApplicationPath("/rest")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();

        resources.add(UserController.class);
        resources.add(HouseController.class);
        resources.add(RentController.class);


        return resources;
    }
}