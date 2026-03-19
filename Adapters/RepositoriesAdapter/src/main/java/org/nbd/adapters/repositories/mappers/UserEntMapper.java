package org.nbd.adapters.repositories.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;
import org.nbd.adapters.repositories.entities.*;
import org.nbd.model.Administrator;
import org.nbd.model.Client;
import org.nbd.model.Employee;
import org.nbd.model.User;


@Mapper(componentModel = "spring")
public interface UserEntMapper {


    ClientEnt toUserEnt(Client client);
    EmployeeEnt toUserEnt(Employee employee);
    AdministratorEnt toUserEnt(Administrator administrator);

    Client toUser(ClientEnt clientEnt);
    Employee toUser(EmployeeEnt employeeEnt);
    Administrator toUser(AdministratorEnt administratorEnt);

    default UserEnt toUserEnt(User user){
        if (user instanceof Client c) return toUserEnt(c);
        if (user instanceof Employee e) return toUserEnt(e);
        if (user instanceof Administrator a) return toUserEnt(a);
        throw new IllegalArgumentException("Unknown User subclass: " + user.getClass());
    }

    default User toUser(UserEnt userEnt){
        if (userEnt instanceof ClientEnt c) return toUser(c);
        if (userEnt instanceof EmployeeEnt e) return toUser(e);
        if (userEnt instanceof AdministratorEnt a) return toUser(a);
        throw new IllegalArgumentException("Unknown UserEnt subclass: " + userEnt.getClass());
    }
}
