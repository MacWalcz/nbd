package org.nbd;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;

public class ClientRepoManager extends JPARepoManager<Client> {
   public ClientRepoManager(String link) {
       super(link);
   }

    @Override
    public Client read(long id) {
        return em.find(Client.class, id);
    }

    @Override
    public void delete(long id) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.remove(em.find(Client.class, id));
        transaction.commit();
    }

    @Override
    public void update(long id, Client entity) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Client client = em.find(Client.class, id, LockModeType.PESSIMISTIC_WRITE);

        client.setFirstName(entity.getFirstName());
        client.setLastName(entity.getLastName());
        client.setPhoneNumber(entity.getPhoneNumber());
        client.setClientType(entity.getClientType());

        em.merge(client);
        transaction.commit();
    }
}
