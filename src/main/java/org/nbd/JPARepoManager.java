package org.nbd;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public abstract class JPARepoManager implements RepoManager<Object> {
    private static EntityManagerFactory emf;
    private EntityManager em ;
    private String link;


    public JPARepoManager(String link) {
        this.link = link;
        emf = Persistence.createEntityManagerFactory(link);
        em = emf.createEntityManager();
    }



}
