package org.nbd;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;

public class ClientTypeRepoManager extends JPARepoManager<ClientType> {
    public ClientTypeRepoManager(String link ){
        super(link);
    }

    @Override
    public ClientType read(long id) {
        return em.find(ClientType.class, id);
    }

    @Override
    public void delete(long id) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.remove(em.find(ClientType.class, id));
        transaction.commit();
    }
}
