package org.nbd;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.Set;
import java.util.HashSet;




@ApplicationPath("/rest")
public class RestApplication {
/*
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();

        // --- 3. Регистрация ваших контроллеров (Resource Classes) ---
        resources.add(UserController.class);
        resources.add(HouseController.class);
        resources.add(RentController.class);

        // --- 4. Регистрация JSON провайдера для ObjectId/Records ---
        // Это исправит проблему с отображением ObjectId как сложного объекта.
        resources.add(JacksonJsonProvider.class);

        return resources;
    }*/
}