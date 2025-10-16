package org.nbd;


import static org.junit.jupiter.api.Assertions.*;



import jakarta.persistence.*;

import org.hibernate.testing.jdbc.SharedDriverManagerTypeCacheClearingIntegrator;
import org.junit.jupiter.api.*;

import java.time.LocalDate;



public class ORMTest {

    private Rent rent;
    private Client client;
    private House house;
    private ClientType clientType;

    private static String link = "ndb";
    private static EntityManagerFactory emf;
    private EntityManager em ;

    private static ClientRepoManager c = new ClientRepoManager(link);
    private static ClientTypeRepoManager cl = new ClientTypeRepoManager(link);
    private static HouseRepoManager h = new HouseRepoManager(link);
    private static RentRepoManager r = new RentRepoManager(link);

    @BeforeAll
    static void init() {

        ClientType ct1 = new Premium();
        Client client1 = new Client("Jan", "Cebula", "504412322", ct1);
        Client client2 = new Client("Adam", "Tomasz", "504420021", ct1);
        Client client3 = new Client("Jack", "Jones", "504420022", ct1);

        House house1 = new House("9a", 1, 1);
        House house2 = new House("9b", 2, 2);
        House house3 = new House("9c", 3, 3);

        Rent rent1 = new Rent(LocalDate.of(2025,6,6), LocalDate.of(2025,6,7), client1, house1);
        Rent rent2 = new Rent(LocalDate.of(2025,6,7), LocalDate.of(2025,6,8), client2, house2);
        Rent rent3 = new Rent(LocalDate.of(2025,6,9), LocalDate.of(2025,6,10), client3, house3);

        House house4 = new House("1",200.0,30.0);

        Client client4 = new Client("Kamil","Kowal","504420021",ct1);

        Rent rent4 = new Rent(LocalDate.of(2025,10,10), LocalDate.of(2025,10,13), client4, house4);
        Rent rent5 = new Rent(LocalDate.of(2025,10,14), LocalDate.of(2025,10,16), client2, house4);

        cl.create(ct1);
        c.create(client1);
        c.create(client2);
        c.create(client3);
        c.create(client4);
        h.create(house1);
        h.create(house2);
        h.create(house3);
        h.create(house4);
        r.create(rent1);
        r.create(rent2);
        r.create(rent3);
        r.create(rent4);
        r.create(rent5);
    }


    @BeforeEach
    void setUp() {
        house = new House("1",200.0,30.0);
        clientType = new Default();
        client = new Client("Kamil","Kowal","504420021",clientType);
        rent = new Rent(LocalDate.of(2025,10,10), LocalDate.of(2025,10,13), client, house);
    }


    @Test
    void testContainerCreate(){
        cl.create(clientType);
        c.create(client);
        h.create(house);
        r.create(rent);
        Client found = c.read(4);
        assertEquals("Kamil", found.getFirstName());
        House found2 = h.read(4);
        assertEquals("1", found2.getHouseNumber());
        Rent found3 = r.read(4);
        assertEquals(LocalDate.of(2025,10,10), found3.getStartDate());
    }

    @Test
    void testContainerUpdate(){
        ClientType luxury = new Luxury();
        cl.create(luxury);
        Client updatedClient = new Client ("Mikita", "Karabeika", "999888777",luxury );
        c.update(2, updatedClient);
        assertEquals("Mikita", c.read(2).getFirstName());
        assertEquals("Karabeika", c.read(2).getLastName());
        assertEquals("999888777", c.read(2).getPhoneNumber());
        assertEquals(luxury,c.read(2).getClientType());

        House updatedHouse = new House("10b",22,23);
        h.update(2, updatedHouse);
        assertEquals("10b", h.read(2).getHouseNumber());
        assertEquals(22, h.read(2).getPrice());
        assertEquals(23, h.read(2).getArea());
    }

    @Test
    void testContainerDelete(){
        r.delete(1);
        h.delete(1);
        c.delete(1);
        assertNull(c.read(1));
        assertNull(h.read(1));
        assertNull(r.read(1));
    }

    @Test
    void testContainerRead(){
        assertEquals("Jan", c.read(1).getFirstName());
        assertEquals("9a", h.read(1).getHouseNumber());
        assertEquals(LocalDate.of(2025,6,6), r.read(1).getStartDate());
    }

    @Test
    void testFailedUpdateRent(){
        assertEquals(LocalDate.of(2025,10,10), r.read(4).getStartDate());
        assertEquals(LocalDate.of(2025,10,14), r.read(5).getStartDate());
        Rent rentUpdated = new Rent(LocalDate.of(2025,10,12), LocalDate.of(2025,10,15), c.read(4), h.read(4));
        r.update(4, rentUpdated);
        assertEquals(LocalDate.of(2025,10,10), r.read(4).getStartDate());
    }
}