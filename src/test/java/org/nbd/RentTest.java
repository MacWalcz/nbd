package org.nbd;


import static org.junit.jupiter.api.Assertions.*;



import jakarta.persistence.*;

import org.junit.jupiter.api.*;

import java.time.LocalDate;



public class RentTest {

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
        assertTrue(rent.getClient().equals(client));
        assertTrue(rent.getHouse().equals(house));
        double cost = rent.getCost();
        assertTrue(cost == 600.0);
    }

    @Test
    void testInsertClient(){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(house);
        transaction.commit();
        House found = em.find(House.class, house.getId());

        assertNotNull(found);
        assertEquals(house.getPrice(), house.getPrice());

    }

}


