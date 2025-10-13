package org.nbd;


import static org.junit.jupiter.api.Assertions.*;



import jakarta.persistence.*;

import org.junit.jupiter.api.*;

import java.time.LocalDate;



public class ORMTest {

    private Rent rent;
    private Client client;
    private House house;

    private static EntityManagerFactory emf;
    private  EntityManager em ;


    @BeforeAll
    static void init() {
        emf = Persistence.createEntityManagerFactory("ndb");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        ClientType defaultType = new Default();
        em.persist(defaultType);

        em.persist(new Client("Jan", "Cebula", "504412322", defaultType));
        em.persist(new Client("Domino", "Jachas", "504992333", defaultType));

        em.getTransaction().commit();
        em.close();
    }


    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        house = new House("1",200.0,30.0);
        client = new Client("Kamil","Kowal","504420021",new Default());
        rent = new Rent(LocalDate.of(2025,10,10), LocalDate.of(2025,10,13), client, house);

    }

    @Test
    void testRent() {
        assertEquals(rent.getClient(), client);
        assertEquals(rent.getHouse(), house);
        double cost = rent.getCost();
        assertEquals(600.0, cost);
    }

    @Test
    void testInsertClient(){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(house);
        transaction.commit();
        House found = em.find(House.class, house.getId());

        assertNotNull(found);
        assertEquals(house.getPrice(), found.getPrice());

    }

    @Test
    void testDeleteClient(){
        EntityTransaction transaction = em.getTransaction();

        Client found = em.find(Client.class, 1);
        assertEquals("Jan", found.getFirstName());

        transaction.begin();
        em.remove(found);
        transaction.commit();

        found = em.find(Client.class, 1);
        assertNull(found);
    }

    @Test
    void testUpdateClient(){
        EntityTransaction transaction = em.getTransaction();

        String newFirstName = "Mikita";
        Client found = em.find(Client.class, 1);
        String oldFirstName = found.getFirstName();

        found.setFirstName(newFirstName);

        transaction.begin();
        em.merge(found);
        transaction.commit();

        found = em.find(Client.class, 1);
        assertEquals(newFirstName, found.getFirstName());

        found.setFirstName(oldFirstName);

        transaction.begin();
        em.merge(found);
        transaction.commit();

        found = em.find(Client.class, 1);
        assertEquals(oldFirstName, found.getFirstName());
    }

    @Test
    void testSelectClient(){
        Client found = em.find(Client.class, 1);
        assertEquals("Jan", found.getFirstName());
    }
}


